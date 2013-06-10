# Definition: sudoers::entry
#
# Description
#  This custom definition is designed to configure entries
#  for sudoers
#
# Parameters:
#  $ensure - (present|absent) 
#  $isgroup - defines whether the entry is a group entry or not.
#  $nopasswd - defines whether an entry will prompt for a password when used.
#  $noexec - If sudo has been compiled with noexec support and the underlying operating system supports it, 
#            the NOEXEC tag can be used to prevent a dynamically-linked executable from running further commands itself.
#  $commands - Array of commands a user is allowed to run (default: ALL)
#
# Actions:
#  - Create entries for Sudoers
#  - Call file-fragment assembly.
#
# Requires:
#  This definition has no requirements.
#
# Sample Usage:
#  sudoers::entry { 'james': 
#    ensure   => 'present',
#    nopasswd => 'true',
#    noexec   => 'true',
#    commands => ['/bin/cat', '/usr/bin/vim'],
#  }
define sudoers::entry(
  $ensure   = 'present',
  $isgroup  = 'false',
  $nopasswd = 'false',
  $noexec   = 'false',
  $commands = ['ALL']
) {
  include sudoers

  file { "${sudoers::params::ss_basedir}/fragment/${name}": 
    ensure  => $ensure,
    content => template('sudoers/sudoers.erb'),
    notify  => Exec['rebuild-sudoers'],
  }
}
