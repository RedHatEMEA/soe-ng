class jbossbrms::config {
  # config security authentication
  file { '/usr/share/jbossas/standalone/deployments/jboss-brms.war/WEB-INF/components.xml':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/jboss-brms-components.xml"
  }

  # enable demo users 
  file { '/usr/share/jbossas/standalone/deployments/jbpm-human-task.war/WEB-INF/web.xml':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/jbpm-human-task-web.xml"
  }

  # designer config
  file { '/usr/share/jbossas/standalone/deployments/designer.war/profiles':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/jbpm.xml"
  }
  
  # brms console users
  file { '/usr/share/jbossas/standalone/configuration/brms-roles.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/brms-roles.properties"
  }
  
  # brms console roles
  file { '/usr/share/jbossas/standalone/configuration/brms-users.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/brms-users.properties"
  }

  # business central config
  file { '/usr/share/jbossas/standalone/deployments/business-central-server.war/WEB-INF/classes/jbpm.console.properties':
      owner => jboss,
      group => jboss,
      source => "puppet:///modules/jbossbrms/business-central-server-jbpm.console.properties"
  }
}
