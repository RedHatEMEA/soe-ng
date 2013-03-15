class sendmail{
	$mailhost=hiera(mailhost)
	File {
        	owner => "root",
        	group => "root",
        	mode => 0644,
 	}
  	file {'/etc/mail/sendmail.mc':
        	content => template("sendmail/sendmail.mc"),
 	}
	exec { 'compile_config':
		cwd => '/etc/mail/',
		command => 'm4 /etc/mail/sendmail.mc > /etc/mail/sendmail.cf',
		require => [ File['/etc/mail/sendmail.mc']],
	    }
	service { 'sendmail':
		ensure => running,
		enable => true,
		require => [ Exec['compile_config']],
	}
		
##	
}
