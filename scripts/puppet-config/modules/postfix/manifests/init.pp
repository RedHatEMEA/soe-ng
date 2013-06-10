# == Define: postfix
#
# Class that will enable the postfix service with the smpt mailhost passed as
# parametres as well as the domain.
#
# === Parameters
# [postfix::mailhost]
# [postfix::maildomain]
#
# === Examples
#
# Provide some examples on how to use this type:
# include postfix
# where in the /etc/puppet-config/hieradb/*.yaml dir
# you would have  postfix::mailhost: - 'server1'
#                 postfix::maildomain: - 'mydomain.com'
# === Authors
#
# Christina Kyriakidou <christina@redhat.com>
#
# === Copyright
#
# Copyright 2013 Christina Kyriakidou
#
class postfix($mailhost,$maildomain){
  File {
          owner => 'root',
          group => 'root',
          mode  => 0644,
  }
  file {'/etc/postfix/main.cf':
          content => template('postfix/main.cf.erb'),
  }
  service { 'postfix':
    ensure => running,
    enable => true,
  }
}
