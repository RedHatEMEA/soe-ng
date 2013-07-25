class jbosseap::config {
  file { '/etc/jbossas':
       ensure => directory,
       owner  => 'jboss',
       group  => 'jboss'
  }
  
  user { jboss:
     ensure     => present,
     managehome => true,
     gid        => 'jboss',
     comment    => 'Ensure JBoss User'
  }
  
  # admin/admin-123
  file { '/usr/share/jbossas/standalone/configuration/mgmt-users.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbosseap/mgmt-users.properties"
  }
  
  # admin/admin-123
  file { '/usr/share/jbossas/standalone/configuration/application-users.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbosseap/application-users.properties"
  }
  
  file { '/usr/share/jbossas/standalone/configuration/application-roles.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbosseap/application-roles.properties"
  }
}
