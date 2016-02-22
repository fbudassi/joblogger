CREATE TABLE joblogger (
  logTime timestamp NOT NULL,
  level varchar(32) NOT NULL,
  logger varchar(255) NOT NULL,
  message varchar(255) NOT NULL,
  sequence integer NOT NULL,
  threadID integer NOT NULL,
  stackTrace varchar(8192));