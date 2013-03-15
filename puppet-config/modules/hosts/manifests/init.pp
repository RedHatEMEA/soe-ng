# Class: hosts
#
# This module manages 'Hosts templates'
#
#==Variables
#    The result of the template will vary bu an ENC
#     that will set the designated_hostname (this is the FQDN) designated_ip
#    and    a facter fact that will set the designated_shortname variables
#
#   [*ensure*]
#     Ensure if present or absent.
#     Default: present
#
#   [*config_file*]
#     'Hosts' file.
#     Only set this, if your platform is not supported or you know, what you're
#     doing.
#     Default: auto-set, platform specific
#
#   [*template*]
#     Template to use.
#     Only set this, if your platform is not supported or you know, what you're
#     doing.
#     Default: auto-set, platform specific
#
# Actions:
#   Manages 'hosts' content.
#
# Requires:
#   Nothing
#
# Sample Usage:
#   class { 'hosts': }
#
# [Remember: No empty lines between comments and class definition]
class hosts {

  case $::osfamily {
    redhat, debian, suse: {
      $config_file = '/etc/hosts'
      $template = 'hosts/hosts.erb'
      $ensure = 'present'
    }
    default: {
      fail("Unsupported platform: ${::operatingsystem}")
    }
  }

  if $ensure == 'present' {
    $ensure_real = 'file'
  } else {
    $ensure_real = 'absent'
  }

  file { $config_file:
    ensure  => $ensure_real,
    owner   => 'root',
    group   => 'root',
    mode    => '0644',
    content => template($template),
  }
}
