#Angstrom minimalist image for lanc device
#gives you a small image with ssh access

ANGSTROM_EXTRA_INSTALL ?= ""
DISTRO_SSH_DAEMON ?= "dropbear"

LANC_EXTRA_INSTALL ?= "haserl uci bash iperf devmem4"
LANC_CUSTOM_INSTALL ?= "lanc-webif lanc-webif-httpd lanc-webif-ctl"

IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

IMAGE_INSTALL = "task-boot \
		 util-linux-ng-mount util-linux-ng-umount \
		 ${DISTRO_SSH_DAEMON} \
		 ${ANGSTROM_EXTRA_INSTALL} \
		 ${LANC_EXTRA_INSTALL} \
		 ${LANC_CUSTOM_INSTALL} \
		 angstrom-version \
			 "

export IMAGE_BASENAME = "lanc-image"
IMAGE_LINGUAS = ""

inherit image

