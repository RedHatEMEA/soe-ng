# == Class: resolv
#
# This class will set the resolv.conf file and it's  only applicable to the
# servers with an /etc/resolv.conf file.
#
# === Parameters
#
# Document parameters here.
#
# [*nameservers*]
#   e.g. "Specify one or more upstream nameservers servers as an array."
#
# === Examples
#  hieradb/common.yaml file contains an array of nameservers
# resolv::nameservers: - 8.8.8.8
#               - 10.6.12.9
#  this will be used when calling
#  include resolv
#  in the site.pp manifest
#
# === Authors
#
# Christina Kyriakidou <christina@redhat.com>
#
# === Copyright
#
# Copyright 2013 Christina Kyriakidou,
class resolv ($nameservers='8.8.8.8'){
  File {
        owner => 'root',
        group => 'root',
        mode => 0644,
  }
  file {'/etc/resolv.conf':
        content => template('resolv/resolv.conf'),
        require => [File['/etc/sysconfig/network']],
  }
}
