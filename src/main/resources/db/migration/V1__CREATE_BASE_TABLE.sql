CREATE TABLE company
(
  id bigserial,
  symbol text NOT NULL,
  name text,
  ipo_year integer,
  sector text,
  industry text,
  last_quote_dt integer,
  create_dt date NOT NULL,
  last_update_dt date NOT NULL,
  
  CONSTRAINT company_pkey PRIMARY KEY (id),
  CONSTRAINT company_symbol_key UNIQUE (symbol)
);

CREATE TABLE company_statistics
(
	id bigserial,
	company_id bigint NOT NULL,
	insider_own_perc double precision,
	inst_own_perc double precision,
	shs_outstand integer,
	shs_float integer,
	create_dt date NOT NULL,
	last_update_dt date NOT NULL,
	
	CONSTRAINT company_statistics_pkey PRIMARY KEY (id),
	CONSTRAINT company_statistics_unique UNIQUE(company_id),
	CONSTRAINT company_statistics_fk FOREIGN KEY(company_id) REFERENCES company(id)
	
);

CREATE TABLE stock_quote
(
	id bigserial,
	company_id bigint NOT NULL,
	quote_date integer NOT NULL,
	open double precision NOT NULL,
	close double precision NOT NULL,
	high double precision NOT NULL,
	low double precision NOT NULL,
	volume integer NOT NULL,
	
	CONSTRAINT stock_quote_pkey PRIMARY KEY (id),
	CONSTRAINT stock_quote_unique UNIQUE(company_id, quote_date),
	CONSTRAINT stock_quote_fk FOREIGN KEY(company_id) REFERENCES company(id)
);

CREATE INDEX stock_quote_company_index ON stock_quote(company_id);
CREATE INDEX stock_quote_date_index ON stock_quote(quote_date);