DESCRIPTION = "lanc kernel"
LICENSE = "GPLv2"

inherit kernel

PV = "2.6.37"

DEFAULT_PREFERENCE_lanc = "1"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-${PV}.tar.bz2;name=kernel"
#FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-lanc"

S = "${WORKDIR}/linux-${PV}"

SRC_URI_append_lanc = " \
	file://0001-lanc-board-file.patch \
	file://0002-lanc-video-codec-fpga-support.patch \
	file://0003-Add-SMC-configuration-for-CS1-fpga-control-chip-sele.patch \
	file://0004-Modified-FPGA-map.patch \
	file://0005-phram-memcpy.patch \
	file://defconfig \
"


do_configure_prepend() {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config
}


do_configure() {
	oe_runmake oldconfig
}
