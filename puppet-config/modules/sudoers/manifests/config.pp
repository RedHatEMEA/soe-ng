# Class: sudoers::config
#
# Description
#  This class sets up the file-fragment pattern, establishes sensible defaults, 
#  and deploys the assembled file to the system.
#
# Parameters:
#  This class takes no default parameters
# 
# Actions:
#  - Creates directories needed for File Fragment (sudoers.d/fragment)
#  - Creates assembled file (sudoers.conf)
#  - Syncs assembled file to OS
#
# Requires:
#  This module has no requirements
#
# Sample Usage:
#  This module should not be called directly.
class sudoers::config {
  File {
    owner => 'root',
    group => 'root',
    mode  => '0660',
  }
  file { $sudoers::params::ss_basedir:
    ensure => directory,
  }
  file { "${sudoers::params::ss_basedir}/fragment":
    ensure  => directory,
    purge   => true,
    recurse => true,
  }
  file { "${sudoers::params::ss_basedir}/fragment/00_sudoers_defaults":
    ensure => file,
    source => 'puppet:///modules/sudoers/sudoers_defaults',
    notify => Exec['rebuild-sudoers'],  
  }
  file { $sudoers::params::ss_sudoers_file:
    ensure  => file,
    mode    => '0440',
    source  => "${sudoers::params::ss_basedir}/sudoers.conf",
    require => Exec['rebuild-sudoers'],
  }
  exec { 'rebuild-sudoers': 
    command     => "/bin/cat ${sudoers::params::ss_basedir}/fragment/* > ${sudoers::params::ss_basedir}/sudoers.conf",
    refreshonly => true,
    subscribe   => File["${sudoers::params::ss_basedir}/fragment"],
  }
}
