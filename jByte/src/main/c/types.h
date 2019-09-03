#pragma once

#include <stdbool.h>
#include <stdint.h>

typedef uint8_t  u1_t;
typedef uint16_t u2_t;
typedef uint32_t u4_t;

typedef struct CpInfo {
	u1_t               tag;
	u2_t               infoSize;
	void*              info;
} CpInfo;

typedef struct Utf8 {
	u2_t               length;
	u1_t*              bytes;
} Utf8;

typedef struct ClassInfo {
	u2_t               nameIndex;
} ClassInfo;

typedef struct MemberInfo {
	u2_t               classIndex;
	u2_t               nameTypeIndex;
} MemberInfo;

typedef struct StringInfo {
	u2_t               stringIndex;
} StringInfo;

typedef struct NameTypeInfo {
	u2_t               nameIndex;
	u2_t               descriptorIndex;
} NameTypeInfo;

typedef enum AttributeKind {
	CONSTANTVALUE,
	CODE,
	STACKMAPTABLE,
	EXCEPTIONS,
	INNERCLASSES,
	ENCLOSINGMETHOD,
	SYNTHETIC,
	SIGNATURE,
	SOURCEFILE,
	SOURCEDEBUGEXTENSION,
	LINENUMBERTABLE,
	LOCALVARIABLETABLE,
	LOCALVARIABLETYPETABLE,
	DEPRECATED,
	RUNTIMEVISIBLEANNOTATIONS,
	RUNTIMEINVISIBLEANNOTATIONS,
	RUNTIMEVISIBLEPARAMETERANNOTATIONS,
	RUNTIMEINVISIBLEPARAMETERANNOTATIONS,
	RUNTIMEVISIBLETYPEANNOTATIONS,
	RUNTIMEINVISIBLETYPEANNOTATIONS,
	ANNOTATIONDEFAULT,
	BOOTSTRAPMETHODS,
	METHODPARAMETERS
} AttributeKind;

typedef struct AttributeInfo {
	u2_t               nameIndex;
	u4_t               length;
	AttributeKind      kind;
	void*              info;
} AttributeInfo;

typedef struct ExceptionTableEntry {
	u2_t               startPC;
	u2_t               endPC;
	u2_t               handlerPC;
	u2_t               catchType;
} ExceptionTableEntry;

typedef struct Code {
	u2_t               maxStack;
	u2_t               maxLocals;
	u4_t               codeLength;

	u2_t               exceptionTableLength;
	ExceptionTableEntry* exceptionTable;
	u2_t               attributesCount;
	AttributeInfo*     attributes;
} Code;

typedef struct SourceFile {
	u2_t               sourceFileIndex;
} SourceFile;

typedef struct FieldOrMethodInfo {
	u2_t               accessFlags;
	u2_t               nameIndex;
	u2_t               descriptorIndex;
	u2_t               attributesCount;
	AttributeInfo*     attributes;
} FieldOrMethodInfo;

typedef struct ClassFile {
	u2_t               minorVersion;
	u2_t               majorVersion;
	u2_t               cpCount;
	CpInfo*            cp;
	u2_t               accessFlags;
	u2_t               thisClass;
	u2_t               superClass;
	u2_t               interfacesCount;
	u2_t*              interfaces;
	u2_t               fieldsCount;
	FieldOrMethodInfo* fields;
	u2_t               methodsCount;
	FieldOrMethodInfo* methods;
	u2_t               attributesCount;
	AttributeInfo*     attributes;
} ClassFile;
