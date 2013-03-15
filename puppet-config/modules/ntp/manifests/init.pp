# == Define: ntp
#
# Class that will enable the ntp service with the  servers passed as
# parametres.
#
# === Parameters
# [*ntp::ntp_servers*]
#  The first two ntp servers for redhat are  used as defaults but using  hiera
#  this parameter can be set there.
#
# === Examples
#
# Provide some examples on how to use this type:
# include ntp
# where in the /etc/puppet-config/hieradb/*.yaml dir
# you would have  ntp::ntp_servers: - 'server1'
#                                   - 'server2'
#                                   - 'server3'
# === Authors
#
# Christina Kyrikaidou <christina@redhat.com>
#
# === Copyright
#
# Copyright 2013 Christina Kyriakidou
#
class ntp ($ntp_servers = ['0.rhel.pool.ntp.org','1.rhel.pool.ntp.org']){
  File{
    owner => 'root',
    group => 'root',
    mode  => 0644,
  }
  file {'/etc/ntp.conf':
    content => template('ntp/ntp.conf.erb'),
  }
  service { 'ntpd':
    ensure => 'running',
    enable => true,
  }
}
