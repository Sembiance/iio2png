#!/bin/bash

rm -f iio2png-1.0.0.tar iio2png-1.0.0.tar.gz
tar -cvf iio2png-1.0.0.tar build.xml src lib iio2png
gzip iio2png-1.0.0.tar
