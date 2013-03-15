# Class: sudoers
#
# Description
#   This module is designed to install and manage Sudo, 
#   Sudo is a program for some Unix and Unix-like computer 
#   operating systems that allows users to run programs 
#   with the security privileges of another user (normally 
#   the superuser, or root). It is an abbreviation for 
#   "substitute user do"
#
#   This module has been built and tested on RHEL systems.
#
# Parameters:
#  This module takes no default parameters. Refer to the Sudoers::Entry 
#  definiton for usage. 
#   
# Actions:
#   This module will install Sudo packages, and setup the File Fragment
#   pattern necessary to assemble the Sudo file.
#
# Requires:
#  - Class[stdlib]. This is Puppet Labs standard library to include additional methods for use within Puppet. [https://github.com/puppetlabs/puppetlabs-stdlib]
#
# Sample Usage:
#  sudoers::entry { 'james': 
#    ensure   => 'present',
#    nopasswd => 'true',
#    noexec   => 'true',
#    commands => ['/bin/cat', '/usr/bin/vim'],
#  }
class sudoers {
  include stdlib
  include sudoers::params

  anchor { 'sudoers::begin': }
  -> class { 'sudoers::package': }
  -> class { 'sudoers::config': }
  -> anchor { 'sudoers::end': }
}
