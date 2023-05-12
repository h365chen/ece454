# SSH and ECE Servers

## Roadmap

- Prepare yourself to use ECE servers with ssh keys

## No pass phrase

### eceterm

Generate private/public key pairs. We use `-t` to specify the algorithm. (You
can use *PuTTY* https://putty.org on Windows)

```sh
ssh-keygen -t rsa
```

Let's use *empty* phrase first

Add `id_rsa.pub` to `~/.ssh/authorized_keys` on remote servers. You can
manually copy-paste it or use the following:

```sh
ssh-copy-id -i ~/.ssh/id_rsa.pub yourid@eceterm.uwaterloo.ca
```

Now you should be able to login to `eceterm` without the password.

```sh
ssh yourid@eceterm.uwaterloo.ca
```

### ecetelsa and eceubuntu

Now try to ssh into eceterm, then ssh into an ecetesla or an eceubuntu server.
(Do you need the password?)

You should see that the content of `~/.ssh/authorized_keys` on these servers is
the same as the one on eceterm. (why?)

Quit then create or add the following to the `~/.ssh/config` file on your
laptop.

```sh
Host eceterm eceterm1 eceterm2 eceterm3
    HostName %h.uwaterloo.ca
    User yourid
    IdentityFile ~/.ssh/id_rsa

Host ecetesla ecetesla0 ecetesla1 ecetesla2 ecetesla3 ecetesla4
    HostName %h.uwaterloo.ca
    User yourid
    ProxyJump eceterm
    IdentityFile ~/.ssh/id_rsa
```

Then retry.

```sh
ssh ecetesla
```

## With pass phrase

Now let's create another private/public key pair `id_rsa2`, but this time we set
a pass phrase.

```sh
ssh-keygen -t rsa  # name it as ~/.ssh/id_rsa2
```

Log into eceterm and remove the existing one in the `~/.ssh/authorized_keys`,
then add `id_rsa2.pub` to it.

```sh
ssh eceterm
vi ~/.ssh/authorized_keys
```

Clean up old ones.

```sh
rm id_rsa id_rsa.pub
```

Log into ecetelsa. Do you need the pass phrase? How many times do you need it?

```sh
ssh ecetesla
```

### ssh-agent

ssh-agent can help here.

```sh
ssh-add id_rsa2
```

Or you can start one automatically

```diff
Host eceterm eceterm1 eceterm2 eceterm3
    HostName %h.uwaterloo.ca
    User yourid
+   AddKeysToAgent yes
-   IdentityFile ~/.ssh/id_rsa
+   IdentityFile ~/.ssh/id_rsa2

Host ecetesla ecetesla0 ecetesla1 ecetesla2 ecetesla3 ecetesla4
    HostName %h.uwaterloo.ca
    User yourid
    ProxyJump eceterm
-   IdentityFile ~/.ssh/id_rsa
+   IdentityFile ~/.ssh/id_rsa2
```

Then log into ecetesla.

```sh
ssh ecetesla
```

`exit` ecetesla, on your laptop, you can check the added keys using

```sh
ssh-add -L
```

To kill agents, search their pids, `pgrep ssh-agent` then do `kill` for each
pid.

*Think: Can you put the line `AddKeysToAgent yes` under the `ecetelsa`
configuration?*

## Agent Forwarding

Now, log into one ecetesla server, then try to log into another ecetelsa
server from there.

```sh
ssh ecetesla0
ssh ecetesla1  # on ecetesla0
```

Do you need a password?

Let's reset and clean the keys in the agent.

```sh
ssh-add -D
```

Agent forwarding is like allow servers to access your local ssh-agent as if they
were already running on the server. Similar to asking a friend to enter their
password so that you can use their computer. ([GitHub
Docs](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/using-ssh-agent-forwarding))

It is enabled in `sshd-config` on ece servers, so what you need to do is to
enable it in `ssh-config`.

```diff
Host eceterm eceterm1 eceterm2 eceterm3
    HostName %h.uwaterloo.ca
    User yourid
    AddKeysToAgent yes
    IdentityFile ~/.ssh/id_rsa2

Host ecetesla ecetesla0 ecetesla1 ecetesla2 ecetesla3 ecetesla4
    HostName %h.uwaterloo.ca
    User yourid
    ProxyJump eceterm
+   ForwardAgent yes
    IdentityFile ~/.ssh/id_rsa2
```

*Think: Can you put the line `ForwardAgent yes` under the `eceterm`
configuration?*


This time we should be able to hop between ecetelsa servers

```sh
ssh ecetesla0
ssh ecetesla1  # on ecetesla0
ssh ecetesla4  # on ecetesla1, can you do this without the password?
```

*Think: How can you make the last line work without the password?*

## VS Code

Once your ssh configuration is good, using VSCode is trivial.

You can find more details here
<https://github.com/jzarnett/ece252/blob/master/lectures/ECELinux-VSCode-Setup.md>.

## Questions

Can you delete `id_rsa2` and `id_rsa2.pub` file after suceed login?

Which one do you prefer, empty or non-empty pass phrase?

## Afterthoughts

It's easy to forget to upload `id_rsa2.pub` to `authorized_keys`.

Move the line `AddKeysToAgent yes` to the `ecetelsa` configuration seems to
work.

Move the line `ForwardAgent yes` to the `eceterm` configuration does not work.

To hop between ecetelsa servers for indefinite times, we need to add
`ForwardAgent yes` into `~/.ssh/config` on ece servers.

Other ece servers can be configured in a similar way. See
https://ece.uwaterloo.ca/Nexus/arbeau/servers/ for possible servers.

It seems administrator privileges is needed if you are using *PuTTY* on Windows.
