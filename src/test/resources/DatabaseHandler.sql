CREATE TABLE joblogger (
  logTime timestamp NOT NULL,
  level integer NOT NULL,
  logger varchar(64) NOT NULL,
  message varchar(255) NOT NULL,
  sequence integer NOT NULL,
  sourceClass varchar(64) NOT NULL,
  sourceMethod varchar(32) NOT NULL,
  threadID integer NOT NULL,
  stackTrace varchar(8192));