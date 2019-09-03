#include <stdio.h>
#include <stdlib.h>
#include "methods.h"
#include "types.h"

int main(int argc, char** argv) {
	ClassFile cf;

	FILE* fp = fopen(argv[1], "rb");
	u4_t u4;

	READ4(fp, u4);
	if (u4 != 0xCAFEBABE) {
		fprintf(stderr, "Not a valid magic number for a Java class file");
		return EXIT_FAILURE;
	}

	READ2(fp, cf.minorVersion);
	READ2(fp, cf.majorVersion);

	READ2(fp, cf.cpCount);
	cf.cp = malloc(cf.cpCount * sizeof(CpInfo));

	CpInfo* cp = cf.cp;
	// Start iterating from index 1, since indexing in bytecode is not zero based
	for (u2_t i = 1 ; i < cf.cpCount ; i++) {
		ClassInfo* cInfo;
		MemberInfo* mInfo;
		StringInfo* strInfo;
		NameTypeInfo* ntInfo;
		Utf8* utf8;

		READ1(fp, cp[i].tag);
		switch (cp[i].tag) {
			case 7 : // CONSTANT_Class
				// TODO is infoSize needed? we have tag
				cp[i].infoSize = sizeof(ClassInfo);
				cInfo = malloc(cp[i].infoSize);
				cp[i].info = cInfo;
				READ2(fp, cInfo->nameIndex);
				break;
			case 9 : // CONSTANT_Fieldref
			case 10: // CONSTANT_Methodref
			case 11: // CONSTANT_InterfaceMethodref
				cp[i].infoSize = sizeof(MemberInfo);
				mInfo = malloc(cp[i].infoSize);
				cp[i].info = mInfo;
				READ2(fp, mInfo->classIndex);
				READ2(fp, mInfo->nameTypeIndex);
				break;
			case 8 : // CONSTANT_String
				cp[i].infoSize = sizeof(StringInfo);
				strInfo = malloc(cp[i].infoSize);
				cp[i].info = strInfo;
				READ2(fp, strInfo->stringIndex);
				break;
			case 3 : // CONSTANT_Integer
				printf("3:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 4 : // CONSTANT_Float
				printf("4:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 5 : // CONSTANT_Long
				printf("5:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 6 : // CONSTANT_Double
				printf("6:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 12: // CONSTANT_NameAndType
				cp[i].infoSize = sizeof(NameTypeInfo);
				ntInfo = malloc(cp[i].infoSize);
				cp[i].info = ntInfo;
				READ2(fp, ntInfo->nameIndex);
				READ2(fp, ntInfo->descriptorIndex);
				break;
			case 1 : // CONSTANT_Utf8
				cp[i].infoSize = sizeof(Utf8);
				utf8 = malloc(cp[i].infoSize);
				cp[i].info = utf8;
				READ2(fp, utf8->length);
				utf8->bytes = malloc(utf8->length);
				READBYTES(fp, utf8->bytes, utf8->length);
				break;
			case 15: // CONSTANT_MethodHandle
				printf("15:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 16: // CONSTANT_MethodType
				printf("16:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
			case 18: // CONSTANT_InvokeDynamic
				printf("18:NOT SUPPORTED YET\n");
				return EXIT_FAILURE;
		}
	}

	READ2(fp, cf.accessFlags);
	READ2(fp, cf.thisClass);
	READ2(fp, cf.superClass);

	READ2(fp, cf.interfacesCount);
	cf.interfaces = malloc(cf.interfacesCount * sizeof(u2_t));
	for (u2_t i = 0 ; i < cf.interfacesCount ; i++) {
		READ2(fp, cf.interfaces[i]);
	}

	READ2(fp, cf.fieldsCount);
	cf.fields = malloc(cf.fieldsCount * sizeof(FieldOrMethodInfo));
	for (u2_t i = 0 ; i < cf.fieldsCount ; i++) {
		READ2(fp, cf.fields[i].accessFlags);
		READ2(fp, cf.fields[i].nameIndex);
		READ2(fp, cf.fields[i].descriptorIndex);
		READ2(fp, cf.fields[i].attributesCount);
		cf.fields[i].attributes = (cf.fields[i].attributesCount == 0) ? NULL : malloc(cf.fields[i].attributesCount * sizeof(AttributeInfo));
		for (u2_t j = 0 ; j < cf.fields[i].attributesCount ; j++) {
			cf.fields[i].attributes[j] = readAttribute(fp, cf.cpCount, cf.cp);
		}
	}

	READ2(fp, cf.methodsCount);
	cf.methods = malloc(cf.methodsCount * sizeof(FieldOrMethodInfo));
	for (u2_t i = 0 ; i < cf.methodsCount ; i++) {
		READ2(fp, cf.methods[i].accessFlags);
		READ2(fp, cf.methods[i].nameIndex);
		READ2(fp, cf.methods[i].descriptorIndex);
		READ2(fp, cf.methods[i].attributesCount);
		cf.methods[i].attributes = (cf.methods[i].attributesCount == 0) ? NULL : malloc(cf.methods[i].attributesCount * sizeof(AttributeInfo));
		for (u2_t j = 0 ; j < cf.methods[i].attributesCount ; j++) {
			cf.methods[i].attributes[j] = readAttribute(fp, cf.cpCount, cf.cp);
		}
	}

	READ2(fp, cf.attributesCount);
	cf.attributes = (cf.attributesCount == 0) ? NULL : malloc(cf.attributesCount * sizeof(AttributeInfo));
	for (u2_t i = 0 ; i < cf.attributesCount ; i++) {
		cf.attributes[i] = readAttribute(fp, cf.cpCount, cf.cp);
	}

	return EXIT_SUCCESS;
}
