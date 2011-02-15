/*
 * devmem2.c: Simple program to read/write from/to any location in memory.
 *
 *  Copyright (C) 2000, Jan-Derk Bakker (jdb@lartmaker.nl)
 *
 *
 * This software has been developed for the LART computing board
 * (http://www.lart.tudelft.nl/). The development has been sponsored by
 * the Mobile MultiMedia Communications (http://www.mmc.tudelft.nl/)
 * and Ubiquitous Communications (http://www.ubicom.tudelft.nl/)
 * projects.
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <signal.h>
#include <fcntl.h>
#include <ctype.h>
#include <termios.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <sys/stat.h>

#define FATAL do { fprintf(stderr, "Error at line %d, file %s (%d) [%s]\n", \
		__LINE__, __FILE__, errno, strerror(errno)); exit(1); } while(0)

#define MAP_SIZE 4096UL
#define MAP_MASK (MAP_SIZE - 1)
#define BUFFER_SIZE 32768

int main(int argc, char **argv) {
	int fd;
	int file;
	void *map_base, *virt_addr; 
	unsigned long read_result, writeval;
	off_t target;
	off_t file_size = 0;
	int access_type = 'w';

	if(argc < 5) {
		fprintf(stderr, "\nUsage:\t%s { device } { address } { type }  { file } { size }\n"
				"\taddress : memory address to act upon\n"
				"\ttype    : access operation type : [r]ead, [w]rite\n"
				"\tfile    : file to be written/read\n\n",
				argv[0]);
		exit(1);
	}

	target = strtoul(argv[2], 0, 0);
	access_type = tolower(argv[3][0]);

	if (argc == 5) {
		struct stat sb;

		switch (access_type) {
			case 'w':
				if (stat(argv[4], &sb) == -1) FATAL;
				file_size = sb.st_size;
				break;

			case 'r':
			default:
				FATAL;
		}

	} else {
		file_size = strtoul(argv[5], 0, 0);
	}

	if((fd = open(argv[1], O_RDWR)) == -1) FATAL;

	if (access_type == 'w' )
		if((file = open(argv[4], O_RDWR | O_CREAT , 0666 )) == -1) FATAL;
	if (access_type == 'r' )
		if((file = open(argv[4], O_RDWR | O_CREAT | O_TRUNC, 0666 )) == -1) FATAL;

	printf("%s opened.\n", argv[1]);
	fflush(stdout);

	/* Map one page */
	file_size = (file_size + 3) & ~3;
	target = (target + 3) & ~3;
	map_base = mmap(0, (file_size + (target & MAP_MASK) + MAP_MASK) & ~MAP_MASK , PROT_READ | PROT_WRITE, MAP_SHARED, fd, target & ~ MAP_MASK);
	if(map_base == (void *) -1) FATAL;
	virt_addr = (char * )map_base + (target & MAP_MASK);
	printf("Memory mapped at address %p. %p\n", map_base, virt_addr);
	fflush(stdout);

	virt_addr = map_base + (target & MAP_MASK);
	switch (access_type) {
		case 'r':
			{
				unsigned int *start = (unsigned int *) virt_addr;
				int total;
				for (total = 0; file_size  > total; total += 4)
				{
					write(file, start++, 4);
				}
			}
			break;
		case 'w':
			{
				unsigned int *start = (unsigned int *) virt_addr;
				int total;
				for (total = 0; file_size  > total; total += 4)
				{
					read(file, start++, 4);
				}
			}
			break;
		default:
			FATAL;
	}
	if(munmap(map_base, MAP_SIZE) == -1) FATAL;
	close(fd);
	return 0;
}

