# == Define: rsyslog
#
# Class that will enable the ntp service with the  servers passed as
# parametres.
#
# === Parameters
# [*rsyslog::log_server_shortname*]
# This parametre can be either the FQDN or the shortname of the server that the
# logs are sent to. It doesn't default to anything as there isn't a default
# server for any environment.
#
# === Examples
#
# include rsyslog
# where in the /etc/puppet-config/hieradb/*.yaml dir
# you would have  rsyslog::log_server_shortname: 'server1'

# === Authors
#
# Christina Kyrikaidou <christina@redhat.com>
#
# === Copyright
#
# Copyright 2013 Christina Kyriakidou
#
class rsyslog ($log_server_shortname='') {
    File{
      owner => 'root',

      group => 'root',
      mode  => 0644,
    }

    file {'/etc/rsyslog.conf':
      content => template('rsyslog/rsyslog.conf')
    }

    service {'syslog':
      ensure => 'stopped',
      enable => false,
    }

    service { 'rsyslog':
      ensure  => 'running',
      enable  => true,
      require => [Service['syslog'], File['/etc/rsyslog.conf']],
    }

}
