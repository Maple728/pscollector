CREATE TABLE IF NOT EXISTS earning_date
(
	id bigserial,
	symbol text NOT NULL,
	report_date integer NOT NULL,
	"time" text,	-- amc or bmo
	
	CONSTRAINT earning_date_pk PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS dividend_date
(
	symbol text,
	report_date integer,
	
	CONSTRAINT dividend_date_pk PRIMARY KEY(symbol, report_date)
);