/* (c) Milo Wimmer 2016, All Rights Reserved */

drop table Users;
drop table Game;
drop table InGame;
drop table Turn;
drop table GameSeek;
drop table Seeking;

drop trigger InGameIncrementer;
drop trigger GameSeektoGame;

drop VIEW TmpInGame;

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

INSERT INTO Game VALUES(0,'Dummy',NULL);
INSERT INTO Users VALUES('Dummy','Yill');
INSERT INTO GameSeek VALUES(0,50,'Dummy');

CREATE VIEW TmpInGame AS
	SELECT *
	FROM InGame;

CREATE TRIGGER GameSeektoGame 
	AFTER INSERT ON Seeking
	WHEN NOT 0<(SELECT a.Players-COUNT(DISTINCT b.Username)
			FROM GameSeek a,Seeking b
			WHERE a.GSID=b.GSID AND new.GSID=a.GSID)
	BEGIN
		/*SELECT COUNT(DISTINCT b.Username)-a.Players 
			FROM GameSeek a,Seeking b
			WHERE a.GSID=b.GSID AND new.GSID=a.GSID;

			MainServTables.sql  */
		
		INSERT INTO Game
		SELECT a.GID+1,c.Name,'EEEEEEEEEEEEAAAAAAAAAIIIIIIIIIOOOOOOOONNNNNNRRRRRRTTTTTTLLLLSSSSUUUUDDDDGGGBBCCMMPPFFHHVVWWYYKJXQZ   '
		FROM Game a,GameSeek c
		WHERE c.GSID=new.GSID AND
			NOT EXISTS(SELECT * FROM Game b WHERE b.GID>a.GID);

		INSERT INTO InGame
		SELECT 'Dummy',a.GID,'',0,0
		FROM Game a
		WHERE NOT EXISTS(SELECT * FROM Game b WHERE b.GID>a.GID);

		INSERT INTO TmpInGame
		SELECT c.Username,a.GID,'',0,0
		FROM Game a,Seeking c
		WHERE c.GSID=new.GSID AND
			NOT EXISTS(SELECT * FROM Game b WHERE b.GID>a.GID);

		DELETE FROM InGame
		WHERE TOrder=0;

		DELETE FROM Seeking
		WHERE GSID=new.GSID;

		DELETE FROM GameSeek
		WHERE GSID=new.GSID;

		INSERT INTO Turn
		SELECT a.GID,0,0,'',
			'                                                                                                                                                                                                                                 '
			,null
		FROM Game a
		WHERE NOT EXISTS(SELECT * FROM Game b WHERE b.GID>a.GID);

	END;

CREATE TRIGGER InGameIncrementer
	INSTEAD OF INSERT ON TmpInGame
	BEGIN
		INSERT INTO InGame
		SELECT new.Username,new.GID,new.Tiles,d.TOrder+1,new.Score
		FROM InGame d
		WHERE d.GID=new.GID AND
			NOT EXISTS(SELECT * FROM InGame b WHERE b.TOrder>d.TOrder
					AND b.GID=d.GID);
	END;


/* TEST DATA

INSERT INTO Users VALUES('Bill','k');
INSERT INTO Users VALUES('Billi','k');
INSERT INTO Users VALUES('Billm','k');
INSERT INTO Users VALUES('Billk','k');

INSERT INTO GameSeek VALUES(0,2,Null);

INSERT INTO Seeking VALUES(0,'Billi');
INSERT INTO Seeking VALUES(0,'Bill');

SELECT * FROM Seeking;
SELECT * FROM Game;
SELECT * FROM InGame; */