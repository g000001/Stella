# Makefile for primal C++ support

# Version: $Id$

SYSTEMS-ROOT = ../..
STELLA-ROOT = ..

# g++ settings:
CXX   = g++
CC    = gcc
CFLAGS = -O2 -g -fPIC
CXXFLAGS = $(CFLAGS)

# Comment GC-SWITCHES and GC-LIB to not use the garbage collector:
GC-SWITCHES = -DSTELLA_USE_GC
GC-CFLAGS = $(GC-SWITCHES) -I$(STELLA-ROOT)/cpp-lib/gc/include
GC-LIB = gc

override INCLUDE += -I$(STELLA-ROOT)/..

CXX-SRCS = cpp-primal.cc
CXX-OBJS = $(CXX-SRCS:.cc=.o) 
HEADERS = $(CXX-SRCS:.cc=.hh)

LIB-DIR  = $(SYSTEMS-ROOT)/lib

all: $(GC-LIB) $(CXX-OBJS)

# Build the garbage collector if necessary (if you want to customize the
# garbage collector, cd to its directory, `configure/make' it by hand with the
# customized switches and then `make' STELLA):
.PHONY: $(GC-LIB)
$(GC-LIB):
	if (test -n "$(GC-SWITCHES)"); then \
	  if (test ! -f ./gc); then \
	     tar xzf gc-*.tar.gz; \
	     mv gc-*[0-9] gc; \
	  fi; \
	  cd gc; \
	  if (test ! -f ./config.status); then \
	     ./configure $(shell if [ $(OS) = "MINGW" ]; then echo --disable-threads; fi); \
	  fi; \
	  $(MAKE) $(MFLAGS) MY_CFLAGS="$(CFLAGS)" CPPFLAGS="$(CFLAGS)"; \
	  cd ..; \
	  cp -p gc/.libs/lib* $(LIB-DIR); \
	fi

#
# Default rules
# 

# default rule for files in CXX-SRCS
.cc.o: $(HEADERS)
	$(CXX) $(CXXFLAGS) $(GC-CFLAGS) -c $(INCLUDE) $<

#
# Cleaning up
#

.PHONY: clean extraclean

clean:
	/bin/rm -f $(PROG) $(LIB) *.o *.a core \#*

extraclean: clean
	/bin/rm -f *~ .*~ \#* .\#*
	cd gc; rm -f $(GC-LIB); $(MAKE) $(MFLAGS) clean
