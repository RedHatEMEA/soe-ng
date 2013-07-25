class jbosseap::cli {
  file { "/tmp/setup.cli":
    mode    => 0600,
    owner   => jboss,
    group   => jboss,
    content => template("jbosseap/setup.cli.erb"),
    before  => Exec['Run CLI script'],
  }

  exec { 'Run CLI script':
    command  => "/usr/share/jbossas/bin/jboss-cli.sh -c --file=/tmp/setup.cli",
    user     => 'root',
    group    => 'root',
    provider => 'posix',
    require  => Service['jbossas'],
  }
}
