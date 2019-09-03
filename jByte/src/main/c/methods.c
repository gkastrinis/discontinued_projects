#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "methods.h"
#include "types.h"

bool utf8EqAscii(Utf8 utf8Str, const char* asciiStr) {
	size_t asciiLength = strlen(asciiStr);
	if (utf8Str.length != asciiLength) return false;

	for (size_t i = 0 ; i < asciiLength ; i++)
		if (utf8Str.bytes[i] != asciiStr[i]) return false;
	return true;
}

AttributeKind attributeNameToKind(Utf8 attributeName) {
	if (utf8EqAscii(attributeName, "ConstantValue")) return CONSTANTVALUE;
	else if (utf8EqAscii(attributeName, "Code")) return CODE;
	else if (utf8EqAscii(attributeName, "StackMapTable")) return STACKMAPTABLE;
	else if (utf8EqAscii(attributeName, "Exceptions")) return EXCEPTIONS;
	else if (utf8EqAscii(attributeName, "InnerClasses")) return INNERCLASSES;
	else if (utf8EqAscii(attributeName, "EnclosingMethod")) return ENCLOSINGMETHOD;
	else if (utf8EqAscii(attributeName, "Synthetic")) return SYNTHETIC;
	else if (utf8EqAscii(attributeName, "Signature")) return SIGNATURE;
	else if (utf8EqAscii(attributeName, "SourceFile")) return SOURCEFILE;
	else if (utf8EqAscii(attributeName, "SourceDebugExtension")) return SOURCEDEBUGEXTENSION;
	else if (utf8EqAscii(attributeName, "LineNumberTable")) return LINENUMBERTABLE;
	else if (utf8EqAscii(attributeName, "LocalVariableTable")) return LOCALVARIABLETABLE;
	else if (utf8EqAscii(attributeName, "LocalVariableTypeTable")) return LOCALVARIABLETYPETABLE;
	else if (utf8EqAscii(attributeName, "Deprecated")) return DEPRECATED;
	else if (utf8EqAscii(attributeName, "RuntimeVisibleAnnotations")) return RUNTIMEVISIBLEANNOTATIONS;
	else if (utf8EqAscii(attributeName, "RuntimeInvisibleAnnotations")) return RUNTIMEINVISIBLEANNOTATIONS;
	else if (utf8EqAscii(attributeName, "RuntimeVisibleParameterAnnotations")) return RUNTIMEVISIBLEPARAMETERANNOTATIONS;
	else if (utf8EqAscii(attributeName, "RuntimeInvisibleParameterAnnotations")) return RUNTIMEINVISIBLEPARAMETERANNOTATIONS;
	else if (utf8EqAscii(attributeName, "RuntimeVisibleTypeAnnotations")) return RUNTIMEVISIBLETYPEANNOTATIONS;
	else if (utf8EqAscii(attributeName, "RuntimeInvisibleTypeAnnotations")) return RUNTIMEINVISIBLETYPEANNOTATIONS;
	else if (utf8EqAscii(attributeName, "AnnotationDefault")) return ANNOTATIONDEFAULT;
	else if (utf8EqAscii(attributeName, "BootstrapMethods")) return BOOTSTRAPMETHODS;
	else if (utf8EqAscii(attributeName, "MethodParameters")) return METHODPARAMETERS;
}

AttributeInfo readAttribute(FILE* fp, u2_t cpCount, CpInfo* cp) {
	AttributeInfo attr;
	READ2(fp, attr.nameIndex);
	READ4(fp, attr.length);

	if (cpCount >= attr.nameIndex && cp[attr.nameIndex].tag != 1) { // CONSTANT_Utf8
		printf("WRONG ATTRIBUTE INDEX\n");
		exit(EXIT_FAILURE);
	}

	// TODO info should go
	void* info = malloc(attr.length);
	Code* code;
	SourceFile* sourceFile;

	switch (attributeNameToKind( *((Utf8*) cp[attr.nameIndex].info) )) {
		case CONSTANTVALUE:
			READBYTES(fp, info, attr.length);
			break;
		case CODE:
			attr.kind = CODE;
			code = malloc(sizeof(Code));
			attr.info = code;
			READ2(fp, code->maxStack);
			READ2(fp, code->maxLocals);
			READ4(fp, code->codeLength);
			// TODO incomplete
			READBYTES(fp, info, code->codeLength);
			READ2(fp, code->exceptionTableLength);
			code->exceptionTable = (code->exceptionTableLength == 0) ? NULL : malloc(code->exceptionTableLength * sizeof(ExceptionTableEntry));
			for (u2_t i = 0 ; i < code->exceptionTableLength ; i++) {
				READ2(fp, code->exceptionTable[i].startPC);
				READ2(fp, code->exceptionTable[i].endPC);
				READ2(fp, code->exceptionTable[i].handlerPC);
				READ2(fp, code->exceptionTable[i].catchType);
				printf("%d %d %d %d\n", code->exceptionTable[i].startPC, code->exceptionTable[i].endPC, code->exceptionTable[i].handlerPC, code->exceptionTable[i].catchType);
			}
			READ2(fp, code->attributesCount);
			code->attributes = (code->attributesCount == 0) ? NULL : malloc(code->attributesCount * sizeof(AttributeInfo));
			for (u2_t i = 0 ; i < code->attributesCount ; i++) {
				code->attributes[i] = readAttribute(fp, cpCount, cp);
			}
			break;
		case STACKMAPTABLE:
			READBYTES(fp, info, attr.length);
			break;
		case EXCEPTIONS:
			READBYTES(fp, info, attr.length);
			break;
		case INNERCLASSES:
			READBYTES(fp, info, attr.length);
			break;
		case ENCLOSINGMETHOD:
			READBYTES(fp, info, attr.length);
			break;
		case SYNTHETIC:
			READBYTES(fp, info, attr.length);
			break;
		case SIGNATURE:
			READBYTES(fp, info, attr.length);
			break;
		case SOURCEFILE:
			attr.kind = SOURCEFILE;
			sourceFile = malloc(sizeof(SourceFile));
			attr.info = sourceFile;
			READ2(fp, sourceFile->sourceFileIndex);
			break;
		case SOURCEDEBUGEXTENSION:
			READBYTES(fp, info, attr.length);
			break;
		case LINENUMBERTABLE:
			READBYTES(fp, info, attr.length);
			break;
		case LOCALVARIABLETABLE:
			READBYTES(fp, info, attr.length);
			break;
		case LOCALVARIABLETYPETABLE:
			READBYTES(fp, info, attr.length);
			break;
		case DEPRECATED:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEVISIBLEANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEINVISIBLEANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEVISIBLEPARAMETERANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEINVISIBLEPARAMETERANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEVISIBLETYPEANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case RUNTIMEINVISIBLETYPEANNOTATIONS:
			READBYTES(fp, info, attr.length);
			break;
		case ANNOTATIONDEFAULT:
			READBYTES(fp, info, attr.length);
			break;
		case BOOTSTRAPMETHODS:
			READBYTES(fp, info, attr.length);
			break;
		case METHODPARAMETERS:
			READBYTES(fp, info, attr.length);
			break;
		default:
			printf("UNKNOWN ATTRIBUTE\n");
			exit(EXIT_FAILURE);
	}
	return attr;
}

