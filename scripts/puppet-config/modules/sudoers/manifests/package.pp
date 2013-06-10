# Class: sudoers::package
#
# Description
#   This class is designed to install Sudo Packages
#
# Parameters:
#   This module takes no parameters
#
# Actions:
#   This class installs Sudo via Packages.
#
# Requires:
#   This module has no requirements.   
#
# Sample Usage:
#   This method should not be called directly.
class sudoers::package {
  @package { $sudoers::params::ss_package:
    ensure => 'present',
    tag    => 'sudoers-package',
  }
  Package<| tag == 'sudoers-package' |>
}