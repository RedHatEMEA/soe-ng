# Class: sudoers::params
#
# Description
#   This class is designed to carry default parameters for 
#   Class: sudoers.  
#
# Parameters:
#  $ss_basedir - base file fragment directory to perform processing
#  $ss_package - packages needed to install sudo on a specific OS.
#  $ss_sudoers_file - location of sudoers file to be used by the system
#
# Actions:
#   This module does not perform any actions.
#
# Requires:
#   This module has no requirements.   
#
# Sample Usage:
#   This method should not be called directly.
class sudoers::params {
  $ss_basedir = '/tmp/sudoers.d'
  
  case $::operatingsystem {
    redhat,centos,fedora,oel: {
      $ss_package      = 'sudo'
      $ss_sudoers_file = '/etc/sudoers'
    }
  }
}
