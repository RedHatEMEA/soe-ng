#!/bin/bash

#simple test search script to check first db entry following ldap configuration using
#setup.sh
#
#
ldapsearch -x -b ou=machines,dc=mydomain,dc=com -v '(cn=vm212)' 

