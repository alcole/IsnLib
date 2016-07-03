IsnLib
======

Library to provide static functions for working with Bibliographic Identifiers (International Standard * Number).
Identifiers should be passed as Strings. ISN is used when the specific Identifier Type is unimportant.

Tested with Java 1.8.0_65

# Supported Identifier Types
* ISBN - International Standard Book Number. Both 10 digit and 13 digit variants 
* ISMN - International Standard Music Number (used for printed music)
* ISSN - International Standard Serial Number
* EANs - International Article Number, if relating to the above ISNs

## ISBN / EAN Structure
ISBN-10s take the form Group / Publisher / Title / Check Digit. Hyphens ('-') are optionally used to separate the groups

ISBN-13s take the same form with the EAN code added (and the check digit recalculated)

* EAN Code is 978 or 979 for books
* Group is used to show the country or language group, 1-5 digits
* Publisher is the assignment to the manifestation's original publisher, 1-7 digits
* Title fills the remaining digits to make the length
* Check digit

### Additional Notes

1. ISBN-10 can take the character 'X' to be the check digit
2. Every ISBN-10 has an ISBN-13 representation, but an ISBN-13 may not have a unique ISBN-10

## ISMN
ISMN are 13 digit codes with the prefix 979-0
 
## ISSN 
ISSNs are 8 digits with an optional hyphen ('-') after the 4th number. The check digit may be the 'X' character. ISSNs may be encoded in a 13 digit EAN with the 977 prefix.

# Unit Tests
JUnit is required for the unit tests

# Usage

Let me know where you are using these functions, I'd be interested in hearing from you.


# Licence

MIT License

Copyright (c) 2016 Alexander Cole

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.