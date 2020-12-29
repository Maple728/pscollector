CREATE TABLE open_interest
(
	id bigserial PRIMARY KEY,
	company_id bigint NOT NULL,
	call_put char NOT NULL, 	-- C or P
	strike double precision NOT NULL,
	expiration integer NOT NULL,
	oi_date integer NOT NULL,
	open double precision NOT NULL,
	high double precision NOT NULL,
	low double precision NOT NULL,
	close double precision NOT NULL,
	volume integer NOT NULL,
	oi integer NOT NULL,
	
	CONSTRAINT open_interest_unique UNIQUE(company_id, oi_date, strike, expiration, call_put),
	CONSTRAINT open_interest_fk FOREIGN KEY(company_id) REFERENCES company(id) 
);

CREATE INDEX open_interest_company_index ON open_interest(company_id);
CREATE INDEX open_interest_date_index ON open_interest(oi_date);