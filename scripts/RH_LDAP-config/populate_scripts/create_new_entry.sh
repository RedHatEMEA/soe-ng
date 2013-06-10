#!/bin/bash
echo "Schema example:"
echo "dn: cn=rh442,ou=machines,dc=mydomain,dc=com"
echo "objectClass: top"
echo "objectClass: applicationProcess"
echo "objectClass: rh"
echo "cn: rh442"
echo "Brand: Lenovo"
echo "Model: x220"
echo "country: Great Britain"
echo "state: London"
echo "site: lt"
echo "businessUnit: NDSA"
echo "designatedIp: 192.168.122.243-eth0"
echo "designatedGateway: 192.168.122.1-eth0"
echo "designatedNetmask: 255.255.255.0-eth0"
echo "designatedHostname: rh442.mydomain.com"
echo "buildVersion: 0.01"
echo "designatedVlan: 801-eth0"
echo "serverType: web"
echo "clusterName: cluster1"
#
#
echo "Would you like to create a new ldiff file for a new entry?"
echo "Type YES to continue"
read response
if [ "Z${response}" = "ZYES" ]
then
    echo "User continuing: The values entered will create a similar ldif file"
else 
    echo "Operation cancelled by user action"
    exit 1
    
fi
echo "Creating new ldif file under /tmp/ldif-files"

mkdir -p /tmp/ldif-files
filename="/tmp/ldif-files/$(date +'%Y%m%d-%H%M%S')-rh.ldif"
response=""

for i in "cn" "Brand" "Model" "country" "state" "site" "businessUnit" "serverType" "clusterName" "designatedHostname" "buildVersion" "stage" "firewall"
do 
#	ldapsearch -x ""
	echo "Enter a ${i}"
        read response
	if [ $i  = "cn" ] 
	then 
		echo "" >> $filename
		echo "dn: cn=$response,ou=machines,dc=mydomain,dc=com" >> $filename
		echo "objectClass: top" >> $filename
		echo "objectClass: applicationProcess" >> $filename
		echo "objectClass: rh" >> $filename
	fi
        if [[ ! -z $response ]] 
	then
		echo "${i}: $response" >> $filename
	else
		echo "${i}:" >> $filename
	fi
	response=""
done

count=0
while ( true ) 
do
	for i in "designatedIp" "designatedGateway" "designatedNetmask" "designatedVlan"
	do
		response="";
		echo "Enter a ${i}"
        	read response
		if [[ ! -z $response ]]
        	then
			echo "${i}: {${count}}$response" >> $filename
        	else
			echo "${i}:" >> $filename
		fi
	done
	echo "Would you like to add another NIC ?"
	echo "Type YES to continue"
	read response
	if [ "Z${response}" = "ZYES" ]
	then
		echo "User continuing"
	else
		
		echo "" >> $filename
		echo "Finished ldiff $filename"
		echo "type :"
		echo "ldapmodify -v -a  -D 'cn=Manager,dc=mydomain,dc=com' -W -f $filename"
		echo "to add it in the database"

		exit 0
	fi		
	count++			
done
