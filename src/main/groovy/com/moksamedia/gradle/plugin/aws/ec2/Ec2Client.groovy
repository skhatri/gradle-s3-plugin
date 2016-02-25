package com.moksamedia.gradle.plugin.aws.ec2

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeAddressesResult
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.DescribeInstancesResult
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation

class Ec2Client {

    private final AmazonEC2Client client;

    public Ec2Client(String awsProfile, String regionName = "us-east-1") {
        client = new AmazonEC2Client(new ProfileCredentialsProvider(awsProfile));
        client.setRegion(Regions.fromName(regionName))
    }

    public DescribeInstancesResult describeInstancesWithName(String name, boolean runningOnly = true) {

        def filters = []

        filters << new Filter("tag:Name", [name])

        if (runningOnly) {
            filters << new Filter("instance-state-name", ["running"])
        }

        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest()
        describeInstancesRequest.setFilters(filters as List<Filter>)

        client.describeInstances(describeInstancesRequest)

    }


    public def getIpsForInstancesWithName(String name, boolean runningOnly = true) {

        DescribeInstancesResult instanceDescriptions = describeInstancesWithName(name, runningOnly)

        def reservations = instanceDescriptions.getReservations()

        if (reservations.size() == 0 ) {
            return []
        }

        def result = []

        reservations.each { Reservation reservation ->

            def instances = reservation.getInstances()

            instances.each { Instance instance ->

                result << [
                        reservationId:reservation.getReservationId(),
                        instanceId:instance.getInstanceId(),
                        state:instance.getState().getName().toString(),
                        privateIp:instance.getPrivateIpAddress(),
                        privateDns:instance.getPrivateDnsName(),
                        publicIp:instance.getPublicIpAddress(),
                        publicDns:instance.getPublicDnsName()
                ]

            }

        }

        result

    }

}
