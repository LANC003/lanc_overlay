DESCRIPTION = "at91bootstrap: loaded into internal SRAM by AT91 BootROM"
SECTION = "bootloaders"

PARALLEL_MAKE = ""

SRC_URI = "TBD"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} DESTDIR=${DEPLOY_DIR_IMAGE} REVISION=${PR}"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	rm -Rf ${S}/binaries
	if ! [ "x${AT91BOOTSTRAP_BOARD}" == "x" ] ; then
		for board in ${AT91BOOTSTRAP_BOARD} ; do
			cp `find ./board/ -name ${board}_defconfig` .config
			oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}"
			oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}" boot
			oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}" install
		done
	else
		cp ${S}/../defconfig ${S}/.config
		oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}"
		oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}" boot
		oe_runmake AT91_CUSTOM_FLAGS="${AT91BOOTSTRAP_FLAGS}" install
	fi

}

PR = "r1"
DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_lanc = "1"


