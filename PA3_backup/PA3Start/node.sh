#!/bin/bash
targets=($(grep -r -l 'extends Node' src))
for t in ${targets[@]}
do
	grep -r 'Type' $t
done
