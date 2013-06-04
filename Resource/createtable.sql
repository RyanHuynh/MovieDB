create table movies(
	id integer NOT NULL AUTO_INCREMENT,
	title varchar(100) NOT NULL,
	year int NOT NULL,
	director varchar(100) NOT NULL,
	banner_url varchar(200),
	trailer_url varchar(200),
	PRIMARY KEY(id),
	FULLTEXT (title,year,director)
)engine=myisam;
create table stars(
	id integer NOT NULL AUTO_INCREMENT,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	dob date,
	photo_url varchar(200),
	PRIMARY KEY(id),
	FULLTEXT (first_name,last_name)
)engine=myisam;
create table stars_in_movies(
	star_id integer NOT NULL,
	movie_id integer NOT NULL,
	FOREIGN KEY (star_id) REFERENCES stars(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id) 
)engine=myisam;
create table genres(
	id integer NOT NULL AUTO_INCREMENT,
	name varchar(32) NOT NULL,
	PRIMARY KEY(id)
)engine=myisam;
create table genres_in_movies(
	genre_id integer NOT NULL,
	movie_id integer NOT NULL,
	FOREIGN KEY (genre_id) REFERENCES genres(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id)
)engine=myisam;
create table creditcards(
	id varchar(20) NOT NULL,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	expiration date NOT NULL,
	PRIMARY KEY(id)
)engine=myisam;
create table customers(
	id integer NOT NULL AUTO_INCREMENT,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	cc_id varchar(20) NOT NULL,
	FOREIGN KEY (cc_id) REFERENCES creditcards(id),
	address varchar(200) NOT NULL,
	email varchar(50) NOT NULL,
	password varchar(20) NOT NULL,
	PRIMARY KEY(id)
)engine=myisam;
create table sales(
	id integer NOT NULL AUTO_INCREMENT,
	customer_id integer NOT NULL,
	movie_id integer NOT NULL,
	FOREIGN KEY (customer_id) REFERENCES customers(id),
	FOREIGN KEY (movie_id) REFERENCES movies(id),
	sale_date date NOT NULL,
	PRIMARY KEY(id)
)engine=myisam;

