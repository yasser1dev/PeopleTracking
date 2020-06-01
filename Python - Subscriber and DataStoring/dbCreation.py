import sqlite3

dbName="PeopleTracking.db"
TableSchema="""
drop table if exists movementData;
Create Table movementData(
id integer primary key autoincrement,
latitude double,
longitude double,
Date_time text,
speed double,
id_device text
)
"""

#Connection
conn=sqlite3.connect(dbName)
curs=conn.cursor()

#Tables
sqlite3.complete_statement(TableSchema)
curs.executescript(TableSchema)

curs.close()
conn.close()