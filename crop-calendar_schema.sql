-- =====================================================
-- AI Climate-Adaptive Crop Calendar - MySQL 8 Schema
-- =====================================================7

CREATE DATABASE IF NOT EXISTS crop_calendar
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE crop_calendar;

-- ---------- Master / lookup tables ----------

CREATE TABLE languages (
  language_id  TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  code         VARCHAR(5)  NOT NULL UNIQUE,
  name         VARCHAR(50) NOT NULL,
  native_name  VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE soil_types (
  soil_type_id   INT AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(50) NOT NULL UNIQUE,
  water_holding  ENUM('LOW','MEDIUM','HIGH') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE locations (
  location_id  INT AUTO_INCREMENT PRIMARY KEY,
  state        VARCHAR(60) NOT NULL,
  district     VARCHAR(60) NOT NULL,
  latitude     DECIMAL(9,6),
  longitude    DECIMAL(9,6),
  UNIQUE KEY uq_location (state, district)
) ENGINE=InnoDB;

CREATE TABLE crops (
  crop_id  INT AUTO_INCREMENT PRIMARY KEY,
  name     VARCHAR(60) NOT NULL UNIQUE,
  season   ENUM('KHARIF','RABI','ZAID','ANNUAL') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE crop_varieties (
  variety_id        INT AUTO_INCREMENT PRIMARY KEY,
  crop_id           INT NOT NULL,
  name              VARCHAR(80) NOT NULL,
  duration_days     SMALLINT NOT NULL,
  drought_tolerant  BOOLEAN NOT NULL DEFAULT FALSE,
  heat_tolerant     BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (crop_id) REFERENCES crops(crop_id)
) ENGINE=InnoDB;

CREATE TABLE crop_stages (
  stage_id     INT AUTO_INCREMENT PRIMARY KEY,
  crop_id      INT NOT NULL,
  stage_name   VARCHAR(80) NOT NULL,
  stage_order  TINYINT NOT NULL,
  start_day    SMALLINT NOT NULL,
  end_day      SMALLINT NOT NULL,
  FOREIGN KEY (crop_id) REFERENCES crops(crop_id)
) ENGINE=InnoDB;

CREATE TABLE activity_rules (
  rule_id        INT AUTO_INCREMENT PRIMARY KEY,
  crop_id        INT NOT NULL,
  stage_id       INT NULL,
  activity_type  ENUM('SOWING','IRRIGATION','FERTILIZER','PEST_MANAGEMENT','HARVESTING') NOT NULL,
  offset_day     SMALLINT NOT NULL,
  min_rain_mm    DECIMAL(6,2) NULL,
  max_rain_mm    DECIMAL(6,2) NULL,
  min_temp_c     DECIMAL(4,1) NULL,
  max_temp_c     DECIMAL(4,1) NULL,
  message_key    VARCHAR(100) NOT NULL,
  FOREIGN KEY (crop_id)  REFERENCES crops(crop_id),
  FOREIGN KEY (stage_id) REFERENCES crop_stages(stage_id)
) ENGINE=InnoDB;

-- ---------- Farmers and farms ----------

CREATE TABLE farmers (
  farmer_id          INT AUTO_INCREMENT PRIMARY KEY,
  full_name          VARCHAR(100) NOT NULL,
  phone              VARCHAR(15)  NOT NULL UNIQUE,
  language_id        TINYINT UNSIGNED NOT NULL,
  phone_type         ENUM('KEYPAD','SMARTPHONE') NOT NULL,
  literacy_level     ENUM('READS_WRITES','BASIC','NON_LITERATE') NOT NULL DEFAULT 'BASIC',
  preferred_channel  ENUM('APP','SMS','IVR') NOT NULL DEFAULT 'SMS',
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (language_id) REFERENCES languages(language_id)
) ENGINE=InnoDB;

CREATE TABLE farms (
  farm_id            INT AUTO_INCREMENT PRIMARY KEY,
  farmer_id          INT NOT NULL,
  location_id        INT NOT NULL,
  soil_type_id       INT NOT NULL,
  village            VARCHAR(80),
  area_acres         DECIMAL(6,2),
  irrigation_source  ENUM('RAINFED','BOREWELL','CANAL','TANK') NOT NULL DEFAULT 'RAINFED',
  FOREIGN KEY (farmer_id)    REFERENCES farmers(farmer_id) ON DELETE CASCADE,
  FOREIGN KEY (location_id)  REFERENCES locations(location_id),
  FOREIGN KEY (soil_type_id) REFERENCES soil_types(soil_type_id)
) ENGINE=InnoDB;

-- ---------- Weather ----------

CREATE TABLE weather_data (
  weather_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  location_id   INT NOT NULL,
  weather_date  DATE NOT NULL,
  rainfall_mm   DECIMAL(6,2),
  temp_min_c    DECIMAL(4,1),
  temp_max_c    DECIMAL(4,1),
  humidity_pct  TINYINT UNSIGNED,
  data_type     ENUM('HISTORICAL','FORECAST','SIMULATED') NOT NULL,
  UNIQUE KEY uq_weather (location_id, weather_date, data_type),
  FOREIGN KEY (location_id) REFERENCES locations(location_id)
) ENGINE=InnoDB;

-- ---------- Crop plan and dynamic calendar ----------

CREATE TABLE crop_plans (
  plan_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  farm_id                INT NOT NULL,
  crop_id                INT NOT NULL,
  variety_id             INT NULL,
  sowing_date            DATE NOT NULL,
  expected_harvest_date  DATE NULL,
  status                 ENUM('PLANNED','ACTIVE','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PLANNED',
  created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (farm_id)    REFERENCES farms(farm_id) ON DELETE CASCADE,
  FOREIGN KEY (crop_id)    REFERENCES crops(crop_id),
  FOREIGN KEY (variety_id) REFERENCES crop_varieties(variety_id)
) ENGINE=InnoDB;

CREATE TABLE calendar_events (
  event_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan_id         BIGINT NOT NULL,
  rule_id         INT NULL,
  activity_type   ENUM('SOWING','IRRIGATION','FERTILIZER','PEST_MANAGEMENT','HARVESTING') NOT NULL,
  planned_date    DATE NOT NULL,
  adjusted_date   DATE NULL,
  status          ENUM('PENDING','DONE','SKIPPED','RESCHEDULED') NOT NULL DEFAULT 'PENDING',
  risk_level      ENUM('LOW','MEDIUM','HIGH') NOT NULL DEFAULT 'LOW',
  confidence_pct  TINYINT UNSIGNED NOT NULL DEFAULT 70,
  reason_key      VARCHAR(100) NULL,
  FOREIGN KEY (plan_id) REFERENCES crop_plans(plan_id) ON DELETE CASCADE,
  FOREIGN KEY (rule_id) REFERENCES activity_rules(rule_id),
  INDEX idx_event_plan_date (plan_id, planned_date)
) ENGINE=InnoDB;

CREATE TABLE alerts (
  alert_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan_id      BIGINT NOT NULL,
  alert_type   ENUM('DELAYED_RAIN','DROUGHT','HEAT','EXCESS_RAIN','PEST','OTHER') NOT NULL,
  severity     ENUM('LOW','MEDIUM','HIGH') NOT NULL,
  message_key  VARCHAR(100) NOT NULL,
  channel      ENUM('APP','SMS','IVR') NOT NULL,
  status       ENUM('PENDING','SENT','FAILED') NOT NULL DEFAULT 'PENDING',
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  sent_at      TIMESTAMP NULL,
  FOREIGN KEY (plan_id) REFERENCES crop_plans(plan_id) ON DELETE CASCADE,
  INDEX idx_alert_status (status)
) ENGINE=InnoDB;

CREATE TABLE what_if_scenarios (
  scenario_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan_id        BIGINT NOT NULL,
  scenario_type  ENUM('MONSOON_DELAY','LOW_RAINFALL','HEAT_WAVE','EXCESS_RAIN') NOT NULL,
  delay_days     SMALLINT NULL,
  result_json    JSON NULL,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (plan_id) REFERENCES crop_plans(plan_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Crop disease detection (photo option) ----------

CREATE TABLE diseases (
  disease_id              INT AUTO_INCREMENT PRIMARY KEY,
  crop_id                 INT NOT NULL,
  name                    VARCHAR(100) NOT NULL,
  symptoms_key            VARCHAR(100) NOT NULL,
  organic_treatment_key   VARCHAR(100) NULL,
  chemical_treatment_key  VARCHAR(100) NULL,
  FOREIGN KEY (crop_id) REFERENCES crops(crop_id)
) ENGINE=InnoDB;

CREATE TABLE disease_scans (
  scan_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  plan_id          BIGINT NOT NULL,
  image_path       VARCHAR(255) NOT NULL,
  disease_id       INT NULL,
  confidence_pct   TINYINT UNSIGNED NULL,
  scanned_offline  BOOLEAN NOT NULL DEFAULT FALSE,
  scanned_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (plan_id)    REFERENCES crop_plans(plan_id) ON DELETE CASCADE,
  FOREIGN KEY (disease_id) REFERENCES diseases(disease_id)
) ENGINE=InnoDB;

-- ---------- Regional language text and voice ----------

CREATE TABLE translations (
  translation_id  INT AUTO_INCREMENT PRIMARY KEY,
  text_key        VARCHAR(100) NOT NULL,
  language_id     TINYINT UNSIGNED NOT NULL,
  text_value      TEXT NOT NULL,
  audio_url       VARCHAR(255) NULL,
  UNIQUE KEY uq_translation (text_key, language_id),
  FOREIGN KEY (language_id) REFERENCES languages(language_id)
) ENGINE=InnoDB;

-- ---------- Keypad phone support (IVR) ----------

CREATE TABLE ivr_call_logs (
  call_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  farmer_id     INT NULL,
  phone         VARCHAR(15) NOT NULL,
  language_id   TINYINT UNSIGNED NULL,
  menu_path     VARCHAR(100),
  duration_sec  INT,
  called_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (farmer_id)   REFERENCES farmers(farmer_id) ON DELETE SET NULL,
  FOREIGN KEY (language_id) REFERENCES languages(language_id)
) ENGINE=InnoDB;

-- ---------- Offline sync ----------

CREATE TABLE sync_queue (
  sync_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
  farmer_id    INT NOT NULL,
  device_id    VARCHAR(64) NOT NULL,
  entity_type  VARCHAR(50) NOT NULL,
  action       ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  payload      JSON NOT NULL,
  client_time  DATETIME NOT NULL,
  synced       BOOLEAN NOT NULL DEFAULT FALSE,
  synced_at    TIMESTAMP NULL,
  FOREIGN KEY (farmer_id) REFERENCES farmers(farmer_id) ON DELETE CASCADE,
  INDEX idx_sync_pending (farmer_id, synced)
) ENGINE=InnoDB;

-- =====================================================
-- Sample seed data
-- =====================================================

INSERT INTO languages (code, name, native_name) VALUES
  ('en','English','English'),
  ('hi','Hindi','हिन्दी'),
  ('te','Telugu','తెలుగు'),
  ('ta','Tamil','தமிழ்'),
  ('kn','Kannada','ಕನ್ನಡ'),
  ('mr','Marathi','मराठी');

INSERT INTO soil_types (name, water_holding) VALUES
  ('Black (Regur)','HIGH'),
  ('Red','LOW'),
  ('Alluvial','MEDIUM'),
  ('Sandy loam','LOW');

INSERT INTO locations (state, district, latitude, longitude) VALUES
  ('Telangana','Hyderabad',17.385000,78.486700),
  ('Maharashtra','Pune',18.520400,73.856700);

INSERT INTO crops (name, season) VALUES
  ('Rice','KHARIF'),
  ('Cotton','KHARIF'),
  ('Groundnut','KHARIF');

INSERT INTO crop_varieties (crop_id, name, duration_days, drought_tolerant, heat_tolerant) VALUES
  (1,'Rice - long duration',140,FALSE,FALSE),
  (1,'Rice - short duration',110,TRUE,TRUE);

INSERT INTO crop_stages (crop_id, stage_name, stage_order, start_day, end_day) VALUES
  (1,'Nursery',1,0,25),
  (1,'Transplanting',2,25,30),
  (1,'Tillering',3,30,60),
  (1,'Panicle initiation',4,60,90),
  (1,'Flowering',5,90,105),
  (1,'Maturity and harvest',6,105,130);

INSERT INTO activity_rules
  (crop_id, stage_id, activity_type, offset_day, min_rain_mm, max_rain_mm, max_temp_c, message_key) VALUES
  (1, 1, 'SOWING',      0,  40, NULL, 38, 'rice_sow_nursery'),
  (1, 3, 'FERTILIZER', 30, NULL,  25, NULL, 'rice_top_dress_n'),
  (1, 5, 'IRRIGATION', 95, NULL, NULL, 36, 'rice_irrigate_flowering'),
  (1, 6, 'HARVESTING',120, NULL,  10, NULL, 'rice_harvest');

INSERT INTO translations (text_key, language_id, text_value) VALUES
  ('rice_sow_nursery',1,'Sow rice nursery after good rain.'),
  ('rice_sow_nursery',2,'अच्छी बारिश के बाद धान की नर्सरी बोएं।'),
  ('rice_sow_nursery',3,'మంచి వర్షం తర్వాత వరి నారు మడి వేయండి.');