import argparse


parser = argparse.ArgumentParser(description='SipHash')
parser.add_argument('--key_file', '-k', help='key file', type=str, required=True)
parser.add_argument('--msg_file', '-m', help='msg file', type=str, required=True)
args = parser.parse_args()


v0 = 0x736f6d6570736575
v1 = 0x646f72616e646f6d
v2 = 0x6c7967656e657261
v3 = 0x7465646279746573

def sipHash(c=2,d=4,h=8,m,k):
    assert len(k) == 16


def sipRound():
        v0 += v1
        v1 = rotl(v1,13)
        v1 ^= v0
        v0 = rotl(v0,32)
        v2 += v3
        v3 = rotl(v3,16)
        v3 ^= v2
        v0 += v3
        v3 = rotl(v3,21)
        v3 ^= v0
        v2 += v1
        v1 = rotl(v1,17)
        v1 ^= v2
        v2 = rotl(v2,32)

def rotl(v,n):
    return (v << n) | (v >> (64 - n))
