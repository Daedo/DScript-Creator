CREATE TABLE IF NOT EXISTS  "LIGATURES" ("LIGATUREID"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"TEXTID"	INTEGER NOT NULL,"TEXT"	TEXT NOT NULL);
CREATE TABLE "FILES" ("LIGATUREID"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"FILEURI" TEXT NOT NULL);

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (1,1,"A");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (1,"Basic\a1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (2,1,"B");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (2,"Basic\b1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (3,1,"C");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (3,"Basic\c1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (4,1,"D");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (4,"Basic\d1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (5,1,"E");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (5,"Basic\e1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (6,1,"F");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (6,"Basic\f1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (7,1,"G");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (7,"Basic\g1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (8,1,"H");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (8,"Basic\h1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (9,1,"I");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (9,"Basic\i1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (10,1,"J");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (10,"Basic\j1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (11,1,"K");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (11,"Basic\k1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (12,1,"L");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (12,"Basic\l1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (13,1,"M");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (13,"Basic\m1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (14,1,"N");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (14,"Basic\n1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (15,1,"O");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (15,"Basic\o1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (16,1,"P");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (16,"Basic\p1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (17,1,"Q");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (17,"Basic\q1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (18,1,"R");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (18,"Basic\r1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (19,1,"S");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (19,"Basic\s1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (20,1,"T");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (20,"Basic\t1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (21,1,"U");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (21,"Basic\u1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (22,1,"V");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (22,"Basic\v1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (23,1,"W");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (23,"Basic\w1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (24,1,"X");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (24,"Basic\x1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (25,1,"Y");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (25,"Basic\y1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (26,1,"Z");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (26,"Basic\z1.dsvg");

INSERT INTO "LIGATURES"("LIGATUREID","TEXTID","TEXT") VALUES (27,2,"T");
INSERT INTO "FILES"("LIGATUREID","FILEURI") VALUES (27,"Basic\t2.dsvg");