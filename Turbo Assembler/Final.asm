include macros.asm
include macros2.asm
include number.asm

.MODEL  LARGE
.386
.STACK 200h

MAXTEXTSIZE equ 50

.DATA

    CTE_CADENA_973763782         db  "IF anidado: num3 > num2",'$', 25 dup (?)
    CTE_CADENA_1649014845         db  "NOT: condicion verdadera",'$', 24 dup (?)
    CTE_CADENA_720506691         db  "ejecucion del gran codigo",'$', 23 dup (?)
    CTE_CADENA_1231866687         db  "num5 = num1",'$', 37 dup (?)
    _valor1         dd  ?
    CTE_CADENA_1972940894         db  "IF: num2 > num1",'$', 33 dup (?)
    _valor3         dd  ?
    CTE_CADENA_51732842         db  "AND: una falsa",'$', 34 dup (?)
    _valor2         dd  ?
    CTE_CADENA_844888893         db  "num4 >= valor2",'$', 34 dup (?)
    CTE_CADENA_269773827         db  "num2 < num1",'$', 37 dup (?)
    CTE_CADENA_1639840585         db  "Procesando datos",'$', 32 dup (?)
    CTE_CADENA_1276388616         db  "num3 <= num2",'$', 36 dup (?)
    CTE_CADENA_1833728273         db  "OR: una verdadera",'$', 31 dup (?)
    CTE_CADENA_1486365816         db  "AND: ambas verdaderas",'$', 27 dup (?)
    CTE_CADENA_113683548         db  "z > d",'$', 43 dup (?)
    CTE_CADENA_770811029         db  "z <= d",'$', 42 dup (?)
    CTE_CADENA_1196506068         db  "compiladores - Ano 2025",'$', 25 dup (?)
    CTE_CADENA_1992119697         db  "Expresion: suma > num6",'$', 26 dup (?)
    CTE_CADENA_656281256         db  "programado - Lenguajes y",'$', 24 dup (?)
    CTE_CADENA_1247189310         db  "FOR con STEP negativo",'$', 27 dup (?)
    CTE_CADENA_1415778202         db  "num1 > num5",'$', 37 dup (?)
    _c1         dd  ?
    CTE_CADENA_1073882882         db  "FOR con STEP positivo",'$', 27 dup (?)
    _c2         dd  ?
    _c3         dd  ?
    CTE_CADENA_694788982         db  "FOR sin STEP (default 1)",'$', 24 dup (?)
    CTE_CADENA_1685477269         db  "ELSE: num2 <= num1",'$', 30 dup (?)
    _num6         dd  ?
    CTE_CADENA_572768483         db  "valor1 > valor2",'$', 33 dup (?)
    CTE_CADENA_1509011558         db  "isZero: expresion = 0",'$', 27 dup (?)
    _contador         dd  ?
    _num1         dd  ?
    _mensaje3         db  MAXTEXTSIZE dup (?),'$'
    _mensaje4         db  MAXTEXTSIZE dup (?),'$'
    CTE_CADENA_1586780520         db  "Aca se comienza la gran",'$', 25 dup (?)
    _num5         dd  ?
    CTE_CADENA_346508047         db  "Ejecutando codigo...",'$', 28 dup (?)
    _num4         dd  ?
    _num3         dd  ?
    _mensaje1         db  MAXTEXTSIZE dup (?),'$'
    CTE_CADENA_1203237536         db  "num5 < num1",'$', 37 dup (?)
    _num2         dd  ?
    _mensaje2         db  MAXTEXTSIZE dup (?),'$'
    CTE_CADENA_1260495838         db  "num5 > num1",'$', 37 dup (?)
    _a         dd  ?
    _b         dd  ?
    _c         dd  ?
    CTE_CADENA_862086423         db  "Bienvenido al programa",'$', 26 dup (?)
    _d         dd  ?
    _e         dd  ?
    CTE_CADENA_1305468443         db  "Expresion: producto > num4",'$', 22 dup (?)
    _h         dd  ?
    CTE_CADENA_90595521         db  "a > b",'$', 43 dup (?)
    CTE_CADENA_1563278517         db  "OR: ambas falsas",'$', 32 dup (?)
    _s         dd  ?
    CTE_CADENA_1372179140         db  "num4 < valor2",'$', 35 dup (?)
    _x         dd  ?
    _y         db  MAXTEXTSIZE dup (?),'$'
    _z         dd  ?
    CTE_CADENA_2069842465         db  "num3 > num2",'$', 37 dup (?)
    CTE_CADENA_464475231         db  "isZero: expresion != 0",'$', 26 dup (?)
    _base         db  MAXTEXTSIZE dup (?),'$'
    _msgOK            db  0DH,0AH,"Se ejecuto OK",'$'
    _float_lit_1507366         dd  10.5
    _float_lit_52409         dd  5.2
    _float_lit_50492         dd  3.7
    _float_lit_55299         dd  8.9
    _temp_0         dd  ?
    _temp_15         dd  ?
    _temp_37         dd  ?
    _temp_1         dd  ?
    _temp_16         dd  ?
    _temp_38         dd  ?
    _temp_2         dd  ?
    _temp_17         dd  ?
    _temp_39         dd  ?
    _temp_3         dd  ?
    _temp_18         dd  ?
    _temp_19         dd  ?
    _temp_8         dd  ?
    _temp_9         dd  ?
    _temp_4         dd  ?
    _temp_5         dd  ?
    _temp_6         dd  ?
    _temp_7         dd  ?
    _temp_30         dd  ?
    _temp_31         dd  ?
    _temp_10         dd  ?
    _temp_32         dd  ?
    _temp_11         dd  ?
    _temp_33         dd  ?
    _temp_12         dd  ?
    _temp_34         dd  ?
    _temp_13         dd  ?
    _temp_35         dd  ?
    _temp_14         dd  ?
    _temp_36         dd  ?
    _temp_26         dd  ?
    _temp_27         dd  ?
    _temp_28         dd  ?
    _temp_29         dd  ?
    _temp_40         dd  ?
    _temp_20         dd  ?
    _temp_21         dd  ?
    _temp_22         dd  ?
    _temp_23         dd  ?
    _temp_24         dd  ?
    _temp_25         dd  ?

.CODE

PUBLIC START
START:
    mov AX,@DATA
    mov DS,AX
    mov es,ax
    mov _temp_0, 5
    mov eax, _temp_0
    mov _num1, eax
    mov _temp_1, 10
    mov eax, _temp_1
    mov _num2, eax
    mov _temp_2, 15
    mov eax, _temp_2
    mov _num3, eax
    mov _temp_3, 20
    mov eax, _temp_3
    mov _num4, eax
    mov _temp_4, 3
    mov eax, _temp_4
    mov _num5, eax
    mov _temp_5, 7
    mov eax, _temp_5
    mov _num6, eax
    mov _temp_6, 100
    mov eax, _temp_6
    mov _valor1, eax
    mov _temp_7, 50
    mov eax, _temp_7
    mov _valor2, eax
    mov _temp_8, 0
    mov eax, _temp_8
    mov _contador, eax
    mov _temp_9, 1
    mov eax, _temp_9
    mov _s, eax
    mov _temp_10, 16
    mov eax, _temp_10
    mov _h, eax
    mov si, OFFSET CTE_CADENA_862086423
    mov di, OFFSET _mensaje1
    STRCPY
    mov si, OFFSET CTE_CADENA_346508047
    mov di, OFFSET _mensaje2
    STRCPY
    mov si, OFFSET CTE_CADENA_1639840585
    mov di, OFFSET _mensaje3
    STRCPY
    displayString CTE_CADENA_1586780520
    newLine 1
    displayString CTE_CADENA_720506691
    newLine 1
    displayString CTE_CADENA_656281256
    newLine 1
    displayString CTE_CADENA_1196506068
    newLine 1
    displayString _mensaje1
    newLine 1
    displayString _mensaje2
    newLine 1
    displayString _mensaje3
    newLine 1
    mov eax, _num1
    cmp eax, _num5
    jle ET_ELSE_1
    jmp ET_IF_0
ET_IF_0:
    displayString CTE_CADENA_1415778202
    newLine 1
    JMP ET_END_2
ET_ELSE_1:
ET_END_2:
    mov eax, _num2
    cmp eax, _num1
    jge ET_ELSE_4
    jmp ET_IF_3
ET_IF_3:
    displayString CTE_CADENA_269773827
    newLine 1
    JMP ET_END_5
ET_ELSE_4:
ET_END_5:
    mov eax, _num1
    cmp eax, _num5
    jle ET_ELSE_7
    jmp ET_AND_9
ET_AND_9:
    mov eax, _num3
    cmp eax, _num2
    jle ET_ELSE_7
    jmp ET_IF_6
ET_IF_6:
    displayString CTE_CADENA_1486365816
    newLine 1
    JMP ET_END_8
ET_ELSE_7:
ET_END_8:
    mov eax, _num2
    cmp eax, _num4
    jle ET_ELSE_11
    jmp ET_AND_13
ET_AND_13:
    mov eax, _num3
    cmp eax, _num2
    jle ET_ELSE_11
    jmp ET_IF_10
ET_IF_10:
    displayString CTE_CADENA_51732842
    newLine 1
    JMP ET_END_12
ET_ELSE_11:
ET_END_12:
    mov eax, _num1
    cmp eax, _num4
    jle ET_OR_17
    jmp ET_IF_14
ET_OR_17:
    mov eax, _num3
    cmp eax, _num2
    jle ET_ELSE_15
    jmp ET_IF_14
ET_IF_14:
    displayString CTE_CADENA_1833728273
    newLine 1
    JMP ET_END_16
ET_ELSE_15:
ET_END_16:
    mov eax, _num1
    cmp eax, _num4
    jle ET_OR_21
    jmp ET_IF_18
ET_OR_21:
    mov eax, _num2
    cmp eax, _num4
    jle ET_ELSE_19
    jmp ET_IF_18
ET_IF_18:
    displayString CTE_CADENA_1563278517
    newLine 1
    JMP ET_END_20
ET_ELSE_19:
ET_END_20:
    mov eax, _num1
    cmp eax, _num4
    jle ET_IF_22
    jmp ET_ELSE_23
ET_IF_22:
    displayString CTE_CADENA_1649014845
    newLine 1
    JMP ET_END_24
ET_ELSE_23:
ET_END_24:
    mov eax, _num1
    add eax, _num2
    mov _temp_11, eax
    mov eax, _temp_11
    cmp eax, _num6
    jle ET_ELSE_26
    jmp ET_IF_25
ET_IF_25:
    displayString CTE_CADENA_1992119697
    newLine 1
    JMP ET_END_27
ET_ELSE_26:
ET_END_27:
    mov eax, _num1
    imul _num5
    mov _temp_12, eax
    mov eax, _temp_12
    cmp eax, _num4
    jle ET_ELSE_29
    jmp ET_IF_28
ET_IF_28:
    displayString CTE_CADENA_1305468443
    newLine 1
    JMP ET_END_30
ET_ELSE_29:
ET_END_30:
    mov _temp_16, 7
    mov eax, _s
    imul _temp_16
    mov _temp_15, eax
    mov _temp_17, 1
    mov eax, _temp_15
    add eax, _temp_17
    mov _temp_14, eax
    mov _temp_19, 2
    mov eax, _h
    cdq
    idiv _temp_19
    mov _temp_18, eax
    mov eax, _temp_14
    sub eax, _temp_18
    mov _temp_13, eax
    cmp _temp_13, 0
    je ET_IF_31
    jmp ET_ELSE_32
ET_IF_31:
    displayString CTE_CADENA_1509011558
    newLine 1
    JMP ET_END_33
ET_ELSE_32:
ET_END_33:
    mov _temp_20, 10
    mov eax, _temp_20
    mov _s, eax
    mov _temp_21, 4
    mov eax, _temp_21
    mov _h, eax
    mov _temp_25, 7
    mov eax, _s
    imul _temp_25
    mov _temp_24, eax
    mov _temp_26, 1
    mov eax, _temp_24
    add eax, _temp_26
    mov _temp_23, eax
    mov _temp_28, 2
    mov eax, _h
    cdq
    idiv _temp_28
    mov _temp_27, eax
    mov eax, _temp_23
    sub eax, _temp_27
    mov _temp_22, eax
    cmp _temp_22, 0
    je ET_IF_34
    jmp ET_ELSE_35
ET_IF_34:
    displayString CTE_CADENA_464475231
    newLine 1
    JMP ET_END_36
ET_ELSE_35:
ET_END_36:
    mov eax, _valor1
    cmp eax, _valor2
    jle ET_ELSE_38
    jmp ET_IF_37
ET_IF_37:
    displayString CTE_CADENA_572768483
    newLine 1
    JMP ET_END_39
ET_ELSE_38:
ET_END_39:
    mov eax, _num3
    cmp eax, _num2
    jle ET_ELSE_41
    jmp ET_IF_40
ET_IF_40:
    displayString CTE_CADENA_2069842465
    newLine 1
    JMP ET_END_42
ET_ELSE_41:
    displayString CTE_CADENA_1276388616
    newLine 1
ET_END_42:
    mov eax, _num4
    cmp eax, _valor2
    jge ET_ELSE_44
    jmp ET_IF_43
ET_IF_43:
    displayString CTE_CADENA_1372179140
    newLine 1
    JMP ET_END_45
ET_ELSE_44:
    displayString CTE_CADENA_844888893
    newLine 1
ET_END_45:
    mov eax, _num2
    cmp eax, _num1
    jle ET_ELSE_47
    jmp ET_IF_46
ET_IF_46:
    displayString CTE_CADENA_1972940894
    newLine 1
    mov eax, _num3
    cmp eax, _num2
    jle ET_ELSE_50
    jmp ET_IF_49
ET_IF_49:
    displayString CTE_CADENA_973763782
    newLine 1
    JMP ET_END_51
ET_ELSE_50:
ET_END_51:
    JMP ET_END_48
ET_ELSE_47:
    displayString CTE_CADENA_1685477269
    newLine 1
ET_END_48:
    mov eax, _num5
    cmp eax, _num1
    jge ET_ELSE_53
    jmp ET_IF_52
ET_IF_52:
    displayString CTE_CADENA_1203237536
    newLine 1
    JMP ET_END_54
ET_ELSE_53:
    mov eax, _num5
    cmp eax, _num1
    jle ET_ELSE_56
    jmp ET_IF_55
ET_IF_55:
    displayString CTE_CADENA_1260495838
    newLine 1
    JMP ET_END_57
ET_ELSE_56:
    displayString CTE_CADENA_1231866687
    newLine 1
ET_END_57:
ET_END_54:
    mov eax, _num1
    mov _num5, eax
    mov eax, _num5
    cmp eax, _num1
    jge ET_ELSE_59
    jmp ET_IF_58
ET_IF_58:
    displayString CTE_CADENA_1203237536
    newLine 1
    JMP ET_END_60
ET_ELSE_59:
    mov eax, _num5
    cmp eax, _num1
    jle ET_ELSE_62
    jmp ET_IF_61
ET_IF_61:
    displayString CTE_CADENA_1260495838
    newLine 1
    JMP ET_END_63
ET_ELSE_62:
    displayString CTE_CADENA_1231866687
    newLine 1
ET_END_63:
ET_END_60:
    fld _float_lit_1507366
    fstp _temp_29
    fld _temp_29
    fstp _a
    fld _float_lit_52409
    fstp _temp_30
    fld _temp_30
    fstp _b
    fld _a
    fld _b
    fxch
    fcomp
    fstsw ax
    ffree st(0)
    sahf
    JBE ET_ELSE_65
    jmp ET_IF_64
ET_IF_64:
    displayString CTE_CADENA_90595521
    newLine 1
    JMP ET_END_66
ET_ELSE_65:
ET_END_66:
    fld _float_lit_50492
    fstp _temp_31
    fld _temp_31
    fstp _d
    fld _float_lit_55299
    fstp _temp_32
    fld _temp_32
    fstp _z
    fld _z
    fld _d
    fxch
    fcomp
    fstsw ax
    ffree st(0)
    sahf
    JBE ET_ELSE_68
    jmp ET_IF_67
ET_IF_67:
    displayString CTE_CADENA_113683548
    newLine 1
    JMP ET_END_69
ET_ELSE_68:
    displayString CTE_CADENA_770811029
    newLine 1
ET_END_69:
    mov _temp_34, 3
    fld _d
    fld _temp_34
    fxch
    fsubp
    fstp _temp_33
    fld _temp_33
    fstp _z
    fld _z
    fld _d
    fxch
    fcomp
    fstsw ax
    ffree st(0)
    sahf
    JBE ET_ELSE_71
    jmp ET_IF_70
ET_IF_70:
    displayString CTE_CADENA_113683548
    newLine 1
    JMP ET_END_72
ET_ELSE_71:
    displayString CTE_CADENA_770811029
    newLine 1
ET_END_72:
    displayString CTE_CADENA_694788982
    newLine 1
    mov _temp_35, 1
    mov eax, _temp_35
    mov _contador, eax
ET_FOR_73:
    mov _temp_36, 5
    mov eax, _contador
    cmp eax, _temp_36
    jg ET_FOR_END_74
    DisplayInteger _contador
    newLine 1
    mov eax, _contador
    add eax, 1
    mov _contador, eax
    jmp ET_FOR_73
ET_FOR_END_74:
    displayString CTE_CADENA_1073882882
    newLine 1
    mov _temp_37, 1
    mov eax, _temp_37
    mov _contador, eax
ET_FOR_75:
    mov _temp_38, 10
    mov eax, _contador
    cmp eax, _temp_38
    jg ET_FOR_END_76
    DisplayInteger _contador
    newLine 1
    mov eax, _contador
    add eax, 2
    mov _contador, eax
    jmp ET_FOR_75
ET_FOR_END_76:
    displayString CTE_CADENA_1247189310
    newLine 1
    mov _temp_39, 10
    mov eax, _temp_39
    mov _contador, eax
ET_FOR_77:
    mov _temp_40, 1
    mov eax, _contador
    cmp eax, _temp_40
    jl ET_FOR_END_78
    DisplayInteger _contador
    newLine 1
    mov eax, _contador
    add eax, -3
    mov _contador, eax
    jmp ET_FOR_77
ET_FOR_END_78:
    displayString _msgOK
    newLine 1
    mov ax, 4C00h
    int 21h

END START
