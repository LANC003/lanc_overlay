DESCRIPTION = "Unified Configuration Interface"
SECTION = "base"
LICENSE = "GPL"

SRC_URI = "http://downloads.openwrt.org/sources/uci-${PV}.tar.gz \
         file://make-cross.patch;apply=yes"

FULL_OPTIMIZATION  = ""
BUILD_OPTIMIZATION = ""

do_compile() {
	oe_runmake all
}

do_install() {
	install -m 0755 -d ${D}${base_sbindir} ${D}${libdir} ${D}${includedir}
	install -m 0755 uci-static ${D}${base_sbindir}/uci
	install -m 0644 uci_config.h uci.h uci_list.h ucimap.h ${D}/${includedir}
	cp -rd libuci*so* ${D}/${libdir}
}
