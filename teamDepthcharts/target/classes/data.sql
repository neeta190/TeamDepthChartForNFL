INSERT INTO sport (name) VALUES ('NFL');
INSERT INTO sport (name) VALUES ('NHL');
INSERT INTO sport (name) VALUES ('NBA');

INSERT INTO team (name, sport_id) VALUES ('Arizona Cardinals', 1);
INSERT INTO team (name, sport_id) VALUES ('Atlanta Falcons', 1);
INSERT INTO team (name, sport_id) VALUES ('Baltimore Ravens', 1);

INSERT INTO position (position, position_name) VALUES ('LWR', 'Left Wide Receiver');
INSERT INTO position (position, position_name) VALUES ('RWR', 'Right Wide Receiver');
INSERT INTO position (position, position_name) VALUES ('LT', 'Left Tackle');
INSERT INTO position (position, position_name) VALUES ('LG', 'Left Guard');
INSERT INTO position (position, position_name) VALUES ('C', 'Center');
INSERT INTO position (position, position_name) VALUES ('RT', 'Right Tackle');
INSERT INTO position (position, position_name) VALUES ('RG', 'Right Guard');
INSERT INTO position (position, position_name) VALUES ('TE', 'Tight end');
INSERT INTO position (position, position_name) VALUES ('QB', 'Quarterback');
INSERT INTO position (position, position_name) VALUES ('RB', 'Running Back');


INSERT INTO player (number, name) VALUES (12, 'Tom Brady');
INSERT INTO player (number, name) VALUES (2, 'Kyle Trask');
INSERT INTO player (number, name) VALUES (11, 'Blaine Gabbert');

INSERT INTO player (number, name) VALUES (13, 'Mike Evans');
INSERT INTO player (number, name) VALUES (1, 'Jaelon Darden');
INSERT INTO player (number, name) VALUES (10, 'Scott Miller');

INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (1, 9, 0);
INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (2, 9, 1);
INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (3, 9, 2);
INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (4, 4, 0);
INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (6, 4, 2);
INSERT INTO playerdepthchart (player_id, position_id, depth) VALUES (5, 4, 1);





