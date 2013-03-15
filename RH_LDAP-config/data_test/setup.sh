#!/bin/bash
##############################################################
# basic script to create and populate an ldap database
# run script initially to create DB dir and start ldap 
# (make sure slapd.conf is in /etc/openldap/
# run again to populate LDAP db with data
# 
# rnunn@redhat.com
#
# version=0.04
#
# ver=0.05 changed for RH generic use (christina@redhat.com)
# ver=0.04 added  edits.   
# ver=0.03 added LinuxVersion entry to initial ldap object.   
# ver=0.02 added dir check and permissions. 
# ver=0.01 initial
# 

LDAPROOT=/var/lib/rh

if [ ! -d $LDAPROOT  ];
   then 
                echo "Creating LDAP db directory: $LDAPROOT" &&\
                mkdir $LDAPROOT &&\
		chown ldap.ldap $LDAPROOT &&\
		echo "Now start ldap to populate DB using /etc/init.d/ldap start " && exit 0
fi

for ldiffile in rh-detailed.ldif ; do
	/usr/sbin/slapadd -f /etc/openldap/slapd.conf -F /etc/openldap/slapd.d -l $ldiffile -n 1
        echo "changing ownership of $LDAPROOT"
        chown -R ldap.ldap $LDAPROOT
done



# test examples, unused seperate testsearch.sh is available.
#ldapsearch -b ou=machines,dc=mydomain,dc=com -v '(cn=vm239)'
