## site.pp ##

# This file (/etc/puppetlabs/puppet/manifests/site.pp) is the main entry point
# used when an agent connects to a master and asks for an updated configuration.
#
# Global objects like filebuckets and resource defaults should go in this file,
# as should the default node definition. (The default node can be omitted
# if you use the console and don't define any other nodes in site.pp. See
# http://docs.puppetlabs.com/guides/language_guide.html#nodes for more on
# node definitions.)

## Active Configurations ##

# PRIMARY FILEBUCKET
# This configures puppet agent and puppet inspect to back up file contents when
# they run. The Puppet Enterprise console needs this to display file contents
# and differences.

# Define filebucket 'main':
#filebucket { 'main':
#  server => 'puppet_master_server',
#  path   => false,
#}

# Make filebucket 'main' the default backup location for all File resources:
#File { backup => 'main' }

# DEFAULT NODE
# Node definitions in this file are merged with node data from the console. See
# http://docs.puppetlabs.com/guides/language_guide.html#nodes for more on
# node definitions.

# The default node definition matches any node lacking a more specific node
# definition. If there are no other nodes in this file, classes declared here
# will be included in every node's catalog, *in addition* to any classes
# specified in the console for that node.

#standalone basic manifest that does doingy things
#currently "default" maps to all systems
#future configs should make more use of classes
#and break out the module structures into seperate class files
#hiera could be used and is encouraged (addon to <Puppet-3.0)
#A sane manifest approach is one that utilises generic stages and
#classes with specific data abstracted through Hiera
#this is just a simple outline for common tasks, ideal for standalone
#Puppet runs.
#

node default {
#This section applies to all clients regardless
#set a site-wide global path so we dont have to specify for each exec
Exec { path => '/usr/bin:/usr/sbin/:/bin:/sbin' }
  stage { 'last': }
  Stage['main'] -> Stage['last']


############### SYSTEM SERVICES SECTIOM #############################
  # Turn on what we want, turn off what we dont want
  # ntp module handles this
  #}
  service { 'sshd':
    ensure => 'running',
    enable => true,
  }
  service { 'crond':
    ensure => 'running',
    enable => true,
  }
  service { 'ip6tables':
    ensure => 'running',
    enable => true,
  }
  service { 'auditd':
    ensure => 'running',
    enable => true,
  }
##################NETWORK DEVICE EXAMPLES################
#    global network settings /etc/sysconfig/network
  network::global { 'default':
    hostname   => $designated_hostname,
    gateway    => $designated_gateway,
  }
  include network
  include motd
  include resolv
  include hosts

  include iptables
  include postfix
  include rsyslog
  ##################### NTP SECTION ##############################
  # Add settings for NTP
  include ntp

  ######################## SYSTEM USERS ##########################
  #add users for system
  $sysusers_all=hiera(sysusers_all)
  $sudoers=hiera(sudousers)
  user { $sysusers_all:
    ensure           => 'present',
    comment          => 'Temporary test user for engineering',
    shell            => '/bin/bash',
    managehome       => true,
  }

  sudoers::entry { $sudoers:
    ensure   => 'present',
    nopasswd => true,
    commands => ['ALL'],
  }

}
