DESCRIPTION = "x-wrt web interface for lanc device"
HOMEPAGE = "http://github.com/geomatsi/lanc-webif"
LICENSE = "GPL"

PR="r1"

SRC_URI = "git://github.com/geomatsi/lanc-webif.git;protocol=git"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RDEPENDS = "uci"
WWW = "/www"
DAT = "/data"

PACKAGES = "${PN}-dbg ${PN}-httpd ${PN}-ctl ${PN} ${PN}-dev"

INITSCRIPT_PACKAGES = "${PN}-httpd ${PN}-ctl"

INITSCRIPT_NAME_${PN}-httpd = "httpd"
INITSCRIPT_PARAMS_${PN}-httpd = "defaults 61"

INITSCRIPT_NAME_${PN}-ctl = "lanc-ctl"
INITSCRIPT_PARAMS_${PN}-ctl = "defaults 65"

inherit update-rc.d

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} usr/bin/webif-page-orig.c -o usr/bin/webif-page.orig
	rm -rf usr/bin/webif-page-orig.c
}

do_install() {
	cp -rd * ${D}
	install -m 0755 -d ${D}/www/data
}


# package the html/js/css/scripts for webif

FILES_${PN}-httpd = " ${sysconfdir}/init.d/httpd "
FILES_${PN}-ctl   = " ${sysconfdir}/init.d/lanc-ctl "

FILES_${PN} = " ${sysconfdir} \
		${base_libdir} \
		${bindir} \
		${libdir} \
		${WWW} \
		${DAT} \
              "

