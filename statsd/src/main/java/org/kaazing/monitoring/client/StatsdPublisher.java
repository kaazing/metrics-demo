/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kaazing.monitoring.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the configuration of StatsD connection
 *
 */
public class StatsdPublisher {

    private static final short STATSD_BUFFER_SIZE = 1500;

    private ByteBuffer sendBuffer;

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsdPublisher.class);

    private final InetSocketAddress address;
    private final DatagramChannel channel;

    /**
     * Constructor that creates a connection to StatsD
     * @param host
     * @param port
     * @throws UnknownHostException
     * @throws IOException
     */
    public StatsdPublisher(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getByName(host), port);
    }

    /**
     * Constructor that creates a connection to StatsD and sets the buffer size
     * @param host
     * @param port
     * @throws IOException
     */
    public StatsdPublisher(InetAddress host, int port) throws IOException {
        address = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
        setBufferSize(STATSD_BUFFER_SIZE);
    }

    /**
     * Sets allocated buffer size to be sent to StatsD
     * @param packetBufferSize
     */
    public synchronized void setBufferSize(short packetBufferSize) {
        if (sendBuffer != null) {
            flush();
        }
        sendBuffer = ByteBuffer.allocate(packetBufferSize);
    }

    /**
     * Adds an item to the buffer and calls the flush method
     * @param stat
     * @return
     */
    public synchronized boolean send(String stat) {
        try {
            final byte[] data = stat.getBytes("utf-8");

            sendBuffer.put(data); // append the data
            flush();

            return true;

        } catch (IOException e) {
            LOGGER.error(
                    String.format("Could not send stat %s to host %s:%d", sendBuffer.toString(), address.getHostName(),
                            address.getPort()), e);
            return false;
        }
    }

    /**
     * Flushes the buffer to StatsD
     * @return - boolean indicating whether the buffer was flushed
     */
    private synchronized boolean flush() {
        final int sizeOfBuffer = sendBuffer.position();

        if (sizeOfBuffer <= 0) {
            return false;
        } // empty buffer

        try {
            // send and reset the buffer
            sendBuffer.flip();
            final int nbSentBytes = channel.send(sendBuffer, address);
            sendBuffer.limit(sendBuffer.capacity());
            sendBuffer.rewind();

            if (sizeOfBuffer != nbSentBytes) {
                LOGGER.warn(String.format("Could not send entirely stat %s to host %s:%d. Only sent %d bytes out of %d bytes",
                        sendBuffer.toString(), address.getHostName(), address.getPort(), nbSentBytes, sizeOfBuffer));
                return false;
            }

        } catch (IOException e) {
            LOGGER.error(
                    String.format("Could not send stat %s to host %s:%d", sendBuffer.toString(), address.getHostName(),
                            address.getPort()), e);
            return false;
        }

        return true;
    }
}
