CREATE TABLE Users (
	Username Text PRIMARY KEY, 
	AuthToken  TEXT NOT NULL);

CREATE TABLE Game (
	GID INT PRIMARY KEY,
	Name TEXT,
	AvalibleTiles TEXT);

CREATE TABLE InGame (
	Username TEXT,
	GID INT,
	Tiles TEXT,
	TOrder INT,
	Score INT,
	PRIMARY KEY(Username,GID),
	FOREIGN KEY(GID) REFERENCES Game(GID),
	FOREIGN KEY(Username) REFERENCES Users(Username));

CREATE TABLE Turn (
	GID INT,
	TurnNum INT,
	Points INT,
	WordPlayed TEXT,
	Boardstate TEXT,
	Player TEXT,
	PRIMARY KEY(GID,TurnNum),
	FOREIGN KEY(GID) REFERENCES Game(GID),
	FOREIGN KEY(Player) REFERENCES Users(Username));

CREATe TABLE GameSeek (
	GSID INT PRIMARY KEY,
	Players INT,
	Name TEXT);

CREATE TABLE Seeking (
	GSID INT,
	Username TEXT,
	PRIMARY KEY(Username,GSID),
	FOREIGN KEY(GSID) REFERENCES GameSeek(GSID),
	FOREIGN KEY(Username) REFERENCES Users(Username));
	