# SOE-NG

A central Hub for all scripts around our new SOE Initiative. We put them on
github because we can manage Issues and do Release Planning

## Ldap Config
===========

The RH_LDAP-config folder contains scripts and schemas to set up an ldap
server to serve as an External Node Classifier in order to produce the
`etc/facter/facts.d/ENC-CONFIG.txt` file that would have key-value pairs
for the following:`/etc/facter/facts.d/ENC-CONFIG.txt` file that would
have key-value pairs for the following:

    designated_hostname=vm212.mydomain.com
    Brand=RHEV
    Model=KVM
    country=GE
    state=Munic
    site=muc
    firewall=normal
    business_unit=GPS
    cluster_name=RHEV
    server_type=free
    build=1.0-1
    stage=dev
    designated_ip=10.32.69.212
    designated_gateway=10.32.69.254
    designated_netmask=255.255.255.0

1. A slapd instance can be created by running the following so that there's access
to all the slapd commands, and the client ldapmodify commands in order to add
and delete entries from the ldap database.

```
$ yum -y install openldap-servers openldap-clients
$ yum -y install httpd python-ldap
```

2. Create a Database folder with ldap ownership and create a `DB_CONFIG` file

```
$ mkdir /var/lib/rh;
$ cp /usr/share/openldap-servers/DB_CONFIG.example /var/lib/rh/DB_CONFIG;
$ chown -R ldap.ldap /var/lib/rh
```


3. Edit the `slapd.conf` and the `rh.schema` to reflect your configuration and dc (i.e. change `dc=mydomain,dc=com` to
`dc=mysubdomain,dc=example,dc=com`). Run `slappasswd` to create a Manager password output and copy it next to rootpw in
the `slapd.conf`. Copy the `slapd.conf` in `/etc/openldap` and the `rh.schema` to the `/etc/openldap/schema` directory.

4. Edit the `populate_scripts/create_new_entry.sh` file to represent the domain changes in the `slapd.conf` file and run it to create
an intial ldif file. Create a test instance with _ALL_ the entries. Change the bottom entry of the `data_test/rh-detailed.ldif` to
the content of the file that you've just created (the location of which will be indicated by the `create_new_entry.sh` output).
Edit the `rh-detailed.ldif` file further so that the dn of all items represents your domain.

5. From the `data_test` directory run the `setup.sh` script. This should populate the database. Make sure that the ownership of all
items in `/var/lib/rh` and `/etc/openldap/slapd.d` is `ldap.ldap` (`chown -Rvvv ldap.ldap`).

6. Use `slaptest` to test the configuration files. Use `slapcat -f /etc/openlap/slapd.conf -F /etc/openldap/slapd.d -n 1` to get the output of the ldap database (`yes` this
should work even if the ldap server is not running yet.

7. Start the `slapd` daemon and search for your entry:

```
$ ldapsearch -b ou=machines,dc=yourdomain,dc=com -v  -x -LLL '(cn=shortname)'
```

8. Edit the `cgi-scripts/gethostdetails.py` to reflect your entry and copy it in the `/var/www/cgi-bin folder`. Start the httpd entry.
9. Use `wget -0  /tmp/hostdetails http://<ldap_server>/cgi-bin/gethostdetails.py?servername=<unique_servername_cn>` to test that you
get the desired output.

##Puppet-config

1. copy the puppet-config folder in `/etc/puppet-config`
2. Install puppet
3. Add your puppet server to the ldap Server above using the `create_new_entry.sh` script and then adding the ldif file created
to the database using `ldapmodify` (see create new entry output). Make sure that:
 - **designatedHostname** is lower case and the FQDN
 - **cn** is the shortname
 - **stage** is dev/qa/prod
 - **serverType** is free/web/app/db
 - you can add more than one nics
 - all the other entries exist (you can put N/A next to them)
4. Create an `/etc/facter/facts.d` folder and add the generated hostdetails file in there under the name `ENC-CONFIG.txt`.
5. Change the `/etc/puppet-config/hieradb` files to reflect the values you need in your organization
6. Make sure that if you need any network/iptables configurations changed that you have console access to the Machine
7. run `puppet apply --config /etc/puppet-config/puppet.conf /etc/puppet-config/manifests/site.pp --noop` to see what will change
8. If you are not happy with what will change edit the `/etc/puppet-config/manifests/site.pp` to your satisfaction
9. Test again and when you are happy, apply all changes

```
$ puppet apply --config /etc/puppet-config/puppet.conf /etc/puppet-config/manifests/site.pp
```
