package com.moksamedia.gradle.plugin.aws.ec2

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Admin on 12/4/15.
 */
public class Ec2ClientTest {

    @Test
    public void testAdHoc() {

        Ec2Client client = new Ec2Client("default")

        def result = client.getIpsForInstancesWithName("DEV-ACCT", false)

        println(result.toString())


    }

}