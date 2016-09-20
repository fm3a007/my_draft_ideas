# 

TARGET = 
TEST = test.bin
LIB = logger
SHARED = lib$(LIB).so
STATIC = lib$(LIB).a

#DEBUG = -ggdb 
DEBUG = -O2 
CXX = g++
CC = gcc
AR = ar
CXXFLAGS = -Wall $(DEBUG) -DWITH_ODBC_OUTPUT -DWITH_THREAD_SAFETY -fPIC 

BASE_PATH = .
SRC_PATH = $(BASE_PATH)/src

INCLUDE = -I$(BASE_PATH)/include -I$(SRC_PATH) -I$(BASE_PATH) -I../../lib/otlv4

LIBP = 
LIBS = -lodbc

SRCS := $(wildcard $(SRC_PATH)/*.cpp)
SRCC := $(wildcard $(SRC_PATH)/*.c)

OBJS := $(patsubst %.cpp, %.o, $(SRCS)) $(patsubst %.c, %.o, $(SRCC))

SRCT := $(wildcard test/*.cpp)
OBJT := $(patsubst %.cpp, %.o, $(SRCT))


#all :  $(TARGET) $(SHARED) $(STATIC) $(TEST) 
all :  $(SHARED) $(STATIC) $(TEST) 

$(TARGET) : $(OBJS) 
	#if ! [ -d bin ] ; then mkdir bin ; fi
	$(CXX) $(CXXFLAGS) $(LIBP) $(LIBS) -o $@ $^ 

$(TEST) : $(OBJS) $(OBJT)
	#if ! [ -d bin ] ; then mkdir bin ; fi
	$(CXX) $(CXXFLAGS) $(LIBP) $(LIBS) -o $@ $^ 

$(SHARED) : $(OBJS)
	#if ! [ -d bin ] ; then mkdir bin ; fi
	$(CXX) -shared $(CXXFLAGS) $(LIBP) $(LIBS) -o $@ $^ 

$(STATIC) : $(OBJS)
	#if ! [ -d bin ] ; then mkdir bin ; fi
	#$(AR) -r $@ $(LIBP) $(LIBS) $^ 
	$(AR)  -r $@ $^ 

%.o : %.cpp
	$(CXX) $(CXXFLAGS) $(INCLUDE) -c -o $@ $^

%.o : %.c
	$(CC) $(CXXFLAGS) $(INCLUDE) -c -o $@ $^

.PHONY:clean
clean:
	rm -rf $(TARGET)  $(SHARED) $(STATIC) $(OBJS) $(TEST) $(OBJT)
	# find . -name *.o -delete


