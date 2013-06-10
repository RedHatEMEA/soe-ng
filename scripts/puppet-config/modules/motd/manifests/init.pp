# == Class: motd
#
# This class will set the /etc/motd  file and it's  only applicable to the
# servers with an /etc/motd file.
# === Variables
# This class is dependent on an ENC that will set the  variables of
# business_unit
# cluster_name
# === Dependencies
# This is dependent on the /etc/sysconfig/network being set
# by the network class.
# === Examples
# include motd if you have a stdlib module
# and you can create an ENC file in /etc/facter/facts.d/ENC-CONFIG.txt
# that will contain the business_unit and the cluster_name as key value pairs
#
# === Authors
#
# Christina Kyriakidou <christina@redhat.com>
#
# === Copyright
#
# Copyright 2013 Christina Kyriakidou,
class motd{

  $config_file = '/etc/motd'
  $template = 'motd/motd.erb'

  file { $config_file:
    ensure  => 'present',
    owner   => 'root',
    group   => 'root',
    mode    => '0644',
    content => template($template),
    require => [File['network.sysconfig','/etc/hosts']],
  }
}
