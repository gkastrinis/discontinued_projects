#pragma once

#include <stdio.h>
#include <netinet/in.h>
#include "types.h"

#define READBYTES(file, var, num) \
	fread(var, 1, num, file)
#define READ(file, var) \
	fread(&(var), sizeof(var), 1, file)
#define READ1(file, var) \
	READ(file, var)
#define READ2(file, var) \
	READ(file, var); \
	var = htons(var)
#define READ4(file, var) \
	READ(file, var); \
	var = htonl(var)

bool utf8EqAscii(Utf8, const char*);
AttributeKind attributeNameToKind(Utf8);
AttributeInfo readAttribute(FILE*, u2_t, CpInfo*);
