DESCRIPTION = "Simple program to read/write from/to any location in memory."
SRC_URI = "file://devmem3.c"
LICENSE = "GPLv2"
PR = "r3"

S = "${WORKDIR}"

do_compile() {
	${CC} -o devmem3 devmem3.c ${CFLAGS} ${LDFLAGS}
}

do_install() {
	install -d ${D}${bindir}
	install devmem3 ${D}${bindir}
}

SRC_URI[md5sum] = "be12c0132a1ae118cbf5e79d98427c1d"
SRC_URI[sha256sum] = "ec382c90af3ef2f49695ff14a4d6521e58ac482c4e29d6c9ebca8768f699c191"
