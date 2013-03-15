#!/usr/bin/python
#
#
# cgi script to populate host variables based on example 
# provided by Aram Kananov. 
#
# rnunn@redhat.com
#
# script can be called in pre section of kickstart system. 
# resulting host information can then be sourced to set 
# host specific kickstart variables.
#
# usage:
# wget -c -O /tmp/hostdetails http://<ldap_server>/cgi-bin/gethostdetails.py?servername=<unique_servername_cn>
#
# e.g:
# the following run on the installing host will get host details for consta and save in /tmp/hostdetails
# wget -c -O /tmp/hostdetails http://localhost/cgi-bin/gethostdetails.py?servername=consta
#
#
# VERSION=0.06
#
#ver=0.6 adding Vlan definintion, Gateway, stage and generalizing (christina@redhat.com)
#ver=0.05 added LinuxVersion for build tracking
#ver=0.03 added single quotes to output to ensure spaces are escaped (rnunn@redhat.com)
#ver=0.02 edit for host DIT and schema (rnunn@redhat.com)
#ver=0.01 Aram Kananov initial structure. 
# 
import sys,getpass
import ldap
import cgi,sys
import re

print "Content-type: text/text\n\n"
form = cgi.FieldStorage()

if not form.has_key("servername"):
	print "ERROR: unique servername parameter is not suplied"
	sys.exit(0)

servername=form["servername"].value

base_dn = "cn=%s,ou=machines,dc=mydomain,dc=com" % servername
con = ldap.initialize('ldap://localhost')
filter = '(objectclass=rh)'
attrs = ['*']
res=con.search_s( base_dn, ldap.SCOPE_SUBTREE, filter, attrs )[0][1]

print "designated_hostname=%s" % res["designatedHostname"][0]
print "Brand=%s"  % res["brand"][0]
print "Model=%s"  % res["model"][0]
print "country=%s" % res["country"][0]
print "state=%s" % res["state"][0]
print "site=%s" % res["site"][0]
print "firewall=%s" % res["firewall"][0]
print "business_unit=%s" % res["businessUnit"][0]
print "cluster_name=%s" % res["clusterName"][0]
print "server_type=%s" % res["serverType"][0]
print "build=%s"  % res["buildVersion"][0]
print "stage=%s" % res["stage"][0]

#for i in designatedI p len(input[0])
num=0
while num < len(res["designatedIp"]): 
	if res["designatedIp"][num].startswith( '{'):
		ipaddr=res["designatedIp"][num].split('}')
		if ipaddr[0].endswith('0') != True :
			print "designated_ip_" + ipaddr[0][-1:] + "=%s" % ipaddr[1]
		else:
			print "designated_ip=%s" % ipaddr[1]
	num+=1
num=0
while num < len(res["designatedGateway"]):
	if res["designatedGateway"][num].startswith( '{'):
		gateway=res["designatedGateway"][num].split('}')
		if gateway[0].endswith('0') != True :
                        print "designated_gateway_" + gateway[0][-1:] + "=%s" % gateway[1]
		else:
			print "designated_gateway=%s" % gateway[1]
	num+=1
num=0
while num < len(res["designatedNetmask"]):
	if res["designatedNetmask"][num].startswith( '{'):
		netmask=res["designatedNetmask"][num].split('}')
		if  netmask[0].endswith('0') != True :
			print "designated_netmask_" + netmask[0][-1:] + "=%s" % netmask[1]
		else:
			print "designated_netmask=%s" % netmask[1]
	elif num > 0:
		
		print "designated_netmask$num=%s" % res["designatedNetmask"][num]
	num+=1

num=0
while num < len(res["designatedVlan"]):
	if res["designatedVlan"][num].startswith( '{'):
		vlan=res["designatedVlan"][num].split('}')
                if  vlan[0].endswith('0') != True :
                        print "designated_vlan_" + vlan[0][-1:] + "=%s" % vlan[1]
		else:
			print "designated_vlan=%s" % lan[1]
	num+=1

con.unbind()

