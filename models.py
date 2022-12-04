from sqlalchemy import Column, String, Integer
from sqlalchemy.sql.expression import text
from sqlalchemy.sql.sqltypes import TIMESTAMP, Boolean

from database import Base

class User(Base):
    __tablename__ = "users"
    
    user_id = Column(Integer, primary_key=True)
    email = Column(String(100), unique=True, nullable=False)
    password = Column(String(100), nullable=False)